package com.qkcare.service;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.domain.MenuVO;
import com.qkcare.domain.PermissionVO;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.User;
import com.qkcare.model.authorization.Permission;
import com.qkcare.model.authorization.Resource;
import com.qkcare.model.authorization.Role;
import com.qkcare.model.authorization.UserRole;
import com.qkcare.util.Utils;

@Service(value="authorizationService")
public class AuthorizationServiceImpl  implements AuthorizationService {
	
	private static final Logger LOGGER = Logger.getLogger(AuthorizationServiceImpl.class);
	
	@Autowired
	GenericService genericService;
	
	
	@Transactional
	public BaseEntity saveUserRoles(User user) {
		Pair<List<Long>, List<Long>> roleIdPairs = this.getRemovedAndNewRoleIds(user);
		
		LOGGER.info(String.format("These Roles were removed %s ", roleIdPairs.getValue0()));
		LOGGER.info(String.format("These Roles were added %s ", roleIdPairs.getValue1()));
		
		int deletedRoleCount = this.deleteRemovedRoles(user);
		
		user.getRoles().removeIf((Role r) -> !roleIdPairs.getValue1().contains(r.getId()));
				
		for (Role role : user.getRoles()) {
			this.genericService.save(new UserRole(user, role));
		}
		
		return user;
		
	}
	
	
	@Transactional
	public BaseEntity savePermissions(Role role) {
		Pair<List<Long>, List<Long>> resourceIdPairs = this.getRemovedAndNewResourceIds(role);
		
		LOGGER.info(String.format("These Resources were removed %s ", resourceIdPairs.getValue0()));
		LOGGER.info(String.format("These Resources were added %s ", resourceIdPairs.getValue1()));
		
		int deletedResourceCount = this.deleteRemovedResources(role);
			
		for (Permission permission : role.getPermissions()) {
			permission.setRole(role);
			this.genericService.save(permission);
		}
		
		return role;
		
	}
	
	public BaseEntity getUserById(Long id) {
		User user = (User) this.genericService.find(User.class, id);
		
		// Get the user roles 
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("UR.USER_ID = ", ":userId", id + "", "Long"));
		String queryStr =  "SELECT R.ROLE_ID, R.NAME, R.DESCRIPTION \r\n" + 
				"FROM ROLE R\r\n" + 
				"JOIN USER_ROLE UR ON R.ROLE_ID = UR.ROLE_ID WHERE 1 = 1 ";
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ", " ");
		List<Role> roles = new ArrayList<Role>();
		
		for (Object[] objects : list) {
			roles.add(new Role(new Long(objects[0].toString()), objects[1].toString(), objects[2].toString()));
		}
		
		user.setRoles(roles);
		
		return user;
	}
	
	public BaseEntity getRoleById(Long id) {
		Role role = (Role) this.genericService.find(Role.class, id);
		
		// Get the role resources 
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("RR.ROLE_ID = ", ":roleId", id + "", "Long"));
		String queryStr =  "SELECT R.RESOURCE_ID, R.NAME, R.URL_PATH, "
				+ "RR.CAN_ADD, RR.CAN_VIEW, RR.CAN_EDIT, RR.CAN_DELETE, RR.PERMISSION_ID \r\n" 
				+ "FROM PERMISSION RR "
				+ "JOIN RESOURCE R ON RR.RESOURCE_ID = R.RESOURCE_ID "
				+ "WHERE 1 = 1 ";
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ", " ");
		List<Permission> permissions = new ArrayList<Permission>();
		
		for (Object[] objects : list) {
			permissions.add(new Permission(Utils.getLongValue(objects[7]), new Role(id, "", ""), 
					new Resource(Utils.getLongValue(objects[0]), Utils.getStrValue(objects[1]), Utils.getStrValue(objects[2])), 
					Utils.getStrValue(objects[3]), Utils.getStrValue(objects[4]), Utils.getStrValue(objects[5]), Utils.getStrValue(objects[6])));
		}
		
		role.setPermissions(permissions);
		
		return role;
	}
	
	
	public Pair<List<MenuVO>, List<PermissionVO>> getUserResources(Long userId, String lang) {
		String language = lang;
		if (StringUtils.isBlank(lang)) {
			language = "en";
		}
	
		// Get the user resources 
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("U.USER_ID = ", "userId", userId + "", "Long"));
		String queryStr =  "SELECT R.NAME AS ROLE_NAME, RE.NAME AS RESOURCE_NAME, RE.URL_PATH, " +
				"M1.MENU_ITEM_ID AS MENU_ID, M1.LABEL AS MENU_LABEL, M1.ICON AS MENU_ICON, " +
				"M2.MENU_ITEM_ID AS PARENT_ID, M2.LABEL AS PARENT_LABEL, M2.ICON AS PARENT_ICON, M1.LEVEL, "
				+ "M1.MI_ORDER AS MI_ORDER, M2.MI_ORDER AS PARENT_MI_ORDER, "
				+ "RR.CAN_ADD, RR.CAN_EDIT, RR.CAN_VIEW, RR.CAN_DELETE\r\n" + 
				"FROM PERMISSION RR\r\n" + 
				"JOIN USER_ROLE UR ON RR.ROLE_ID = UR.ROLE_ID\r\n" + 
				"JOIN ROLE R ON UR.ROLE_ID = R.ROLE_ID\r\n" + 
				"JOIN USERS U ON UR.USER_ID = U.USER_ID\r\n" + 
				"JOIN RESOURCE RE ON RR.RESOURCE_ID = RE.RESOURCE_ID\r\n" + 
				"LEFT OUTER JOIN MENU_ITEM M1 ON RE.RESOURCE_ID = M1.RESOURCE_ID AND M1.LANGUAGE = '"  + language + "'\r\n" + 
				"LEFT OUTER JOIN MENU_ITEM M2 ON M1.PARENT_ID = M2.MENU_ITEM_ID AND M2.LANGUAGE = '"  + language + "'\r\n"
				+ "WHERE 1 = 1 ";
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ORDER BY M1.LEVEL, M1.MI_ORDER ", "  ");
		
		Map<String, MenuVO> menuMap = new HashMap<>();
		List<PermissionVO> nonMenuPermissions = new ArrayList<>();
		
		for (Object[] objects : list) {
			String roleName = Utils.getStrValue(objects[0]);
			String resourceName = Utils.getStrValue(objects[1]);
			String urlPath = Utils.getStrValue(objects[2]);
			Long menuId = Utils.getLongValue(objects[3]);
			String label = Utils.getNullStrValue(objects[4]);
			String icon = Utils.getStrValue(objects[5]);
			Long parentMenuId = Utils.getLongValue(objects[6]);
			String parentLabel = Utils.getNullStrValue(objects[7]);
			String parentIcon = Utils.getStrValue(objects[8]);
			Long level = Utils.getLongValue(objects[9]);
			Integer order = Utils.getIntegerValue(objects[10]);
			Integer parentOrder = Utils.getIntegerValue(objects[11]);
			String canAdd = Utils.getStrValue(objects[12]);
			String canEdit = Utils.getStrValue(objects[13]);
			String canView = Utils.getStrValue(objects[14]);
			String canDelete = Utils.getStrValue(objects[15]);
			
			if (label != null) {
				if ( parentLabel != null) {
					
					MenuVO parentMenu = menuMap.get(parentLabel);
					
					if (menuMap.get(parentLabel) == null) {
						parentMenu = new MenuVO(parentMenuId, parentLabel, null, parentIcon, parentOrder);
						menuMap.put(parentLabel, parentMenu);
					}
					
					parentMenu.addItem(new MenuVO(menuId, label, Arrays.asList(urlPath), icon, order));
				} else {
					menuMap.put(label, new MenuVO(menuId, label, Arrays.asList(urlPath), icon, order));
				}
			} 
			nonMenuPermissions.add(new PermissionVO(null, resourceName, canAdd, canEdit, canView, canDelete));
			
			
		}
		
		List<MenuVO> results = new ArrayList<MenuVO>(menuMap.values());
		results.sort(Comparator.comparingLong(MenuVO::getmIOrder));
		
		return Pair.with(results, nonMenuPermissions);
	}
	
	
	
	
	
	private int deleteRemovedRoles(User user) {
		List<Role> selectedRoles = user.getRoles();
		List<Long> selectedRoleIds = selectedRoles.stream()
                .map(Role::getId).collect(Collectors.toList());
		
		String SELECTED_ROLE_STR = "				AND UR.ROLE_ID NOT IN (:selectedRoleIds)\r\n";
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("UR.USER_ID = ", "userId", user.getId() + "", "Long"));

		if (selectedRoleIds.size() == 0) {
			SELECTED_ROLE_STR = "";
		} else {
			paramTupleList.add(Quartet.with("UR.ROLE_ID NOT IN ", "selectedRoleIds", selectedRoleIds.toString().substring(1, selectedRoleIds.toString().length() - 1) + "", "List"));
		}
		
		String queryStr =  "DELETE FROM USER_ROLE WHERE USER_ROLE_ID IN (\r\n" + 
					"			SELECT UR_ID FROM (\r\n" + 
					"				SELECT UR.USER_ROLE_ID AS UR_ID FROM USER_ROLE UR\r\n" + 
					"				WHERE UR.USER_ID = :userId \r\n" +
					SELECTED_ROLE_STR + 
					"			) AS UR2\r\n" + 
					"		) ";
		
		
		Integer totalDeleted = genericService.deleteNativeByCriteria(queryStr, paramTupleList);
		
		LOGGER.info(String.format("Number of user roles deleted %d ", totalDeleted));
		
		
		
		return totalDeleted;
	}
	
	
	private int deleteRemovedResources(Role role) {
		List<Permission> selectedRoleResources = role.getPermissions();
		List<Long> selectedResourceIds = selectedRoleResources.stream()
                .map(rr -> rr.getResource().getId()).collect(Collectors.toList());
		
		String SELECTED_RESOURCE_STR = "				AND RR.RESOURCE_ID NOT IN (:selectedResourceIds)\r\n";
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("RR.ROLE_ID = ", "roleId", role.getId() + "", "Long"));

		if (selectedResourceIds.size() == 0) {
			SELECTED_RESOURCE_STR = "";
		} else {
			paramTupleList.add(Quartet.with("RR.RESOURCE_ID NOT IN ", "selectedResourceIds", 
					selectedResourceIds.toString().substring(1, selectedResourceIds.toString().length() - 1) + "", "List"));
		}
		
		String queryStr =  "DELETE FROM PERMISSION WHERE PERMISSION_ID IN (\r\n" + 
					"			SELECT RR_ID FROM (\r\n" + 
					"				SELECT RR.PERMISSION_ID AS RR_ID FROM PERMISSION RR\r\n" + 
					"				WHERE RR.ROLE_ID = :roleId \r\n" +
					SELECTED_RESOURCE_STR + 
					"			) AS RR2\r\n" + 
					"		) ";
		
		
		Integer totalDeleted = genericService.deleteNativeByCriteria(queryStr, paramTupleList);
		
		LOGGER.info(String.format("Number of user resources deleted %d ", totalDeleted));
		
		return totalDeleted;
	}
	
	
	private Pair<List<Long>, List<Long>> getRemovedAndNewRoleIds(User user) {
		List<Role> selectedRoles = user.getRoles();
		List<Long> selectedRoleIds = selectedRoles.stream().map(Role::getId).collect(Collectors.toList());
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("UR.USER_ID = ", ":userId", user.getId() + "", "Long"));
		String queryStr =  "SELECT UR.ROLE_ID, UR.USER_ID \r\n" + 
				"FROM USER_ROLE UR\r\n" + 
				"WHERE 1 = 1 \r\n";
		
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ", " ");
		List<Long> existingRoleIds = new ArrayList<Long>();
		
		for (Object[] objects : list) {
			existingRoleIds.add(new Long(objects[0].toString()));
		}
		
		
		return Pair.with(new ArrayList<Long>(CollectionUtils.subtract(existingRoleIds, selectedRoleIds)), 
				new ArrayList<Long>(CollectionUtils.subtract(selectedRoleIds, existingRoleIds)));
	}

	
	private Pair<List<Long>, List<Long>> getRemovedAndNewResourceIds(Role role) {
		List<Permission> selectedResources = role.getPermissions();
		List<Long> selectedResourceIds = selectedResources.stream().map(
				rr -> rr.getResource().getId()).collect(Collectors.toList());
				
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("RR.ROLE_ID = ", ":roleId", role.getId() + "", "Long"));
		String queryStr =  "SELECT RR.RESOURCE_ID, RR.ROLE_ID \r\n" + 
				"FROM PERMISSION RR\r\n" + 
				"WHERE 1 = 1 \r\n";
		
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ", " ");
		List<Long> existingResourceIds = new ArrayList<Long>();
		
		for (Object[] objects : list) {
			existingResourceIds.add(new Long(objects[0].toString()));
		}
		
		
		return Pair.with(new ArrayList<Long>(CollectionUtils.subtract(existingResourceIds, selectedResourceIds)), 
				new ArrayList<Long>(CollectionUtils.subtract(selectedResourceIds, existingResourceIds)));
	}

}
