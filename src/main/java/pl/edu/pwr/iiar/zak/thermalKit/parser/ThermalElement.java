package pl.edu.pwr.iiar.zak.thermalKit.parser;

public class ThermalElement {

    private int row;
    private int col;
    private String name;
    private String type;

    public ThermalElement(String name, String type, int col, int row) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ThermalElement)) {
            return false;
        }

        ThermalElement that = (ThermalElement) other;

        return this.row == that.row && this.col == that.col &&
                this.name.equals(that.name) && this.type.equals(that.type);
    }
}
