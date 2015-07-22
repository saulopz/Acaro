package chatbot;

public class Variable {
    private Boolean global;
    private String name;
    private String value;

    public Variable(Boolean global, String name, String value) {
	this.global = global;
	this.name = name;
	this.value = value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public Boolean isGlobal() {
	return global;
    }

    public String getName() {
	return name;
    }

    public String getValue() {
	return value;
    }

    public String toString() {
	String out = "";
	out += global ? "+" : " ";
	out += name + "=" + value;
	return out;
    }
}
