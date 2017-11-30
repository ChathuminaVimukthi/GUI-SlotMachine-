package sample;

public class Symbol implements ISymbol{
    //initializing instance variables
    private String image;
    private int value;

    //overloaded construcutor
    public Symbol(String image,int value) {
        this.image = image;
        this.value = value;
    }

    //overidden methods from ISymbol interface
    @Override
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
