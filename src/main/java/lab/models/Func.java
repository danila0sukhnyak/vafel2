package lab.models;

import lab.interfaces.IFunc;

public class Func {
    IFunc func;
    IFunc dfunc;
    String name;

    public Func(String name, IFunc func, IFunc dfunc) {
        this.name = name;
        this.func = func;
        this.dfunc = dfunc;
    }

    /*
    Getter and Setter
     */
    public IFunc getFunc() { return func; }
    public void setFunc(IFunc func) { this.func = func; }
    public IFunc getDfunc() { return dfunc; }
    public void setDfunc(IFunc dfunc) { this.dfunc = dfunc; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
