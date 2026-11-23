package org.kpfl.model.person;

public abstract class Person {
	protected String id;		//20 (10 * 2) bytes
	protected String name;		//40 (20 * 2) bytes
	protected int age;			//4 bytes
	protected char gender;		//2 bytes TOTAL: 66 BYTES

	public Person() {
		this.id = String.format("%-10s", "");
		this.name = String.format("%-20s", "");
		this.age = 0;
		this.gender = '?';
	}

	public Person(Person p) {
		if (p != null) {
			this.id = String.format("%-10s", p.id);
			this.name = String.format("%-20s", p.name);
			this.age = p.age;
			this.gender = p.gender;
		}
	}

	public Person(String id, String name, int age, char gender) {
		this.id = String.format("%-10s", id != null ? id.trim() : "");
		this.name = String.format("%-20s", name != null ? name.trim() : "");
		this.age = age;
		this.gender = gender;
	}

	public Person(String id, String name, int age, String gender) {
		this.id = String.format("%-10s", id != null ? id.trim() : "");
		this.name = String.format("%-20s", name != null ? name.trim() : "");
		this.age = age;
		this.gender = (gender != null && !gender.trim().isEmpty()) ? gender.trim().toUpperCase().charAt(0) : '?';
	}

	public String getId() {
		return id.trim();
	}

	public void setId(String id) {
		this.id = String.format("%-10s", id != null ? id.trim() : "");
	}

	public String getName() {
		return name.trim();
	}

	public void setName(String name) {
		this.name = String.format("%-20s", name != null ? name.trim() : "");
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public void setGender(String gender) {
		this.gender = (gender != null && !gender.trim().isEmpty()) ? gender.trim().toUpperCase().charAt(0) : '?';
	}

	public abstract String getDetails();

	public void display() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		return "ID: " + id.trim() + 
				"\nName: " + name.trim() + 
				"\nAge: " + age + 
				"\nGender: " + gender + 
				"\n";
	}
}
