package main.java.model;

import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;

public class PantryData {

	private final SimpleStringProperty name = new SimpleStringProperty();
	private final SimpleStringProperty brand = new SimpleStringProperty();
	private final SimpleStringProperty stock = new SimpleStringProperty();
	private final SimpleStringProperty expiration = new SimpleStringProperty();
	private final SimpleStringProperty cost = new SimpleStringProperty();
	private final SimpleStringProperty category = new SimpleStringProperty();
	

	public PantryData(@NamedArg("name") String name, @NamedArg("brand") String brand, @NamedArg("stock") String stock, @NamedArg("expiration") String expiration, @NamedArg("cost") String cost, @NamedArg("category") String category){
		setName(name);
		setBrand(brand);
		setStock(stock);
		setExpiration(expiration);
		setCost(cost);
		setCategory(category);
	}
	
	public String getName(){
		return name.get();
	}
	
	public void setName(String name){
		this.name.set(name);
	}
	
	public String getBrand(){
		return brand.get();
	}
	
	public void setBrand(String brand){
		this.brand.set(brand);
	}
	
	public String getStock(){
		return stock.get();
	}
	
	public void setStock(String stock){
		this.stock.set(stock);
	}
	
	public String getExpiration(){
		return expiration.get();
	}
	
	public void setExpiration(String expiration){
		this.expiration.set(expiration);
	}
	
	public String getCost(){
		return cost.get();
	}
	
	public void setCost(String cost){
		this.cost.set(cost);
	}
	
	public String getCategory(){
		return category.get();
	}
	
	public void setCategory(String category){
		this.category.set(category);
	}
}
