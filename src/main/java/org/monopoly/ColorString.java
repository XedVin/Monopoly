package org.monopoly;
public class ColorString {
    public static enum Color{
        Negro,
        Rojo,
        Verde,
        Amarillo,
        Azul,
        Magenta,
        Cian,
        Blanco,
        Normal
    }
    public static enum PrintMode{
        Reset,
        Bold,
        Dim,
        Italic,
        Underline

    }

    private String content;
    private Color foreground;
    private Color background;
    private PrintMode printMode;

    private static final String ANSI_ESC = "\u001B[";
    private static final String ANSI_RESET = "\u001B[0m";

    public ColorString(String content){
        this.content = content;
    };
    public ColorString setForeground(Color foreground){
        this.foreground = foreground;
        return this;
    }
    public ColorString setBackground(Color background){
        this.background = background;
        return this;
    }
    public ColorString setPrintMode(PrintMode mode){
        this.printMode = mode;
        return this;

    }

    public ColorString underline(){
        this.printMode = PrintMode.Underline;
        return this;
    }    
    public ColorString red(){
        this.foreground = Color.Rojo;
        return this;
    }
    public ColorString green()
    {
        this.foreground = Color.Verde;
        return this;
    }

    public ColorString yellow()
    {
        this.foreground = Color.Amarillo;
        return this;
    }

    public ColorString blue()
    {
        this.foreground = Color.Azul;
        return this;
    }

    public ColorString magenta()
    {
        this.foreground = Color.Magenta;
        return this;
    }

    public ColorString cyan()
    {
        this.foreground = Color.Cian;
        return this;
    }

    public ColorString white()
    {
        this.foreground = Color.Blanco;
        return this;
    }
    public ColorString normal(){
        this.foreground = null;
            return this;
    }
    private String getBackgroundEscape(){
        if(this.background != null){
            return "4" + this.background.ordinal();
        }
        return "";
    }
    private String getForegroundEscape(){
        if(this.foreground != null){
            return "3" + this.foreground.ordinal();
        }
        return "";
    }
    private String getPrintModeEscape(){
        if(this.printMode != null){
            return "" + this.printMode.ordinal();
        }
        return "";
    }
    private String getEscapeString(){
        if(this.foreground != null && this.background != null && this.printMode != null){
            return ANSI_ESC + this.getPrintModeEscape() + ";" +this.getForegroundEscape() + ";" + this.getBackgroundEscape() + "m";
        }else if(this.printMode != null && (this.background != null || this.foreground != null)){
            return ANSI_ESC + this.getPrintModeEscape() + ";" +this.getForegroundEscape() + this.getBackgroundEscape() + "m";
        }else if (this.foreground != null && this.background != null){
            return ANSI_ESC + this.getForegroundEscape() + ";" + this.getBackgroundEscape() + "m";
        }
        else return ANSI_ESC + getPrintModeEscape() + this.getForegroundEscape() + this.getBackgroundEscape() + "m";
    }

    @Override
    public String toString() {
        return this.getEscapeString() + content + ANSI_RESET;
    }
}

