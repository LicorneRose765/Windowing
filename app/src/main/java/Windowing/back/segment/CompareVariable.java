package Windowing.back.segment;

public enum CompareVariable {
    X,Y;

    public static CompareVariable getOpposite(CompareVariable var) {
        if (var == X) {
            return Y;
        } else {
            return X;
        }
    }
}
