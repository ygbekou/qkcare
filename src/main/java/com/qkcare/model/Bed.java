package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BED")
public class Bed extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "BED_ID")
	private Long id;	
	@ManyToOne
	@JoinColumn(name = "ROOM_ID")
	private Room room;
	@ManyToOne
	@JoinColumn(name = "CATEGORY_ID")
	private Category category;
	@Column(name = "BED_NUMBER")
	private String bedNumber;
	private Double rate;
	private String description;
	private int status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getBedNumber() {
		return bedNumber;
	}
	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	

	// Overriding equals() to compare two Complex objects
    @Override
    public boolean equals(Object o) {
 
        // If the object is compared with itself then return true  
        if (o == this) {
            return true;
        }
 
        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Bed)) {
            return false;
        }
         
        // typecast o to Complex so that we can compare data members 
        Bed e = (Bed) o;
         
        // Compare the data members and return accordingly 
        return Long.compare(id, e.id) == 0;
    }
    
    
    // Transient
    public String getRoomName() {
 		return this.getRoom().getName();
 	}
 	
    public String getFloorName() {
 		return this.getRoom().getFloor().getName();
 	}
 	
 	public String getBuildingName() {
 		return this.getRoom().getFloor().getBuilding().getName();
 	}
 	
 	public String getBedCategoryName() {
 		return this.getCategory().getName();
 	}
 	
 	public String getStatusDesc() {
 		return status == 0 ? "Active" : "Inactive";
 	}
}
