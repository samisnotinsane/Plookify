package account;

import javafx.beans.property.*;

import javax.rmi.CORBA.Util;
import java.util.Calendar;
import java.util.Date;

public class Device {
	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty name = new SimpleStringProperty();
	private LongProperty addedAt = new SimpleLongProperty();
	private IntegerProperty type = new SimpleIntegerProperty();
	private StringProperty addedAtString = new SimpleStringProperty();

	public String getName() {
		return this.name.get();
	}

	/**
	 * 
	 * @param name
	 */
	private void setName(String name) {
		this.name.set(name);
	}

	public long getAddedAt() {
		return this.addedAt.get();
	}

	/**
	 * 
	 * @param addedAt
	 */
	private void setAddedAt(long addedAt) {
		this.addedAt.set(addedAt);
	}

	public int getType() {
		return this.type.get();
	}

	/**
	 * 
	 * @param type
	 */
	private void setType(int type) {
		this.type.set(type);
	}

	public StringProperty getNameProperty(){
		return name;
	}

	public LongProperty getAddedAtProperty(){
		return addedAt;
	}

	public StringProperty getTypeProperty(){
		if(getType() == 0){
			return new SimpleStringProperty("Desktop");
		}else if(getType() == 1){
			return new SimpleStringProperty("Console");
		}else if(getType() == 2){
			return new SimpleStringProperty("Mobile");
		}
		return new SimpleStringProperty("Unknown");
	}

	public StringProperty getAddedAtString(){
		Calendar calendar = Calendar.getInstance();
		//System.out.println(getAddedAt());
		calendar.setTimeInMillis(getAddedAt());
		addedAtString.set(calendar.getTime().toString());
		return addedAtString;
	}

	public boolean addDevice() {
		String singleQuery;
		singleQuery = "INSERT INTO DEVICES(USERID, NAME, ADDEDAT, TYPE) VALUES('" +
				Utilities.getUserID(Authenticator.getUser().getEmail()) + "','" +
				this.getName() + "','" +
				this.getAddedAt() + "','" +
				this.getType() + "')";
		Utilities.executeSingleQuery(singleQuery);
		return true;
	}

	/**
	 * 
	 * @param namep
	 * @param addedAtp
	 * @param typep
	 */
	public Device(String namep, long addedAtp, int typep, int id) {
		this.setName(namep);
		this.setAddedAt(addedAtp);
		this.setType(typep);
		this.setId(id);
	}

	public int getId() {
		return id.get();
	}

	public StringProperty idProperty() {
		return new SimpleStringProperty(getId() + "");
	}

	public void setId(int id) {
		this.id.set(id);
	}
}