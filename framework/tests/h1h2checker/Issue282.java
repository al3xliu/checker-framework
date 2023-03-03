import org.checkerframework.framework.testchecker.h1h2checker.quals.*;

public class Issue282 {
    // Declared constructor type is not consistent with default from class.
    @SuppressWarnings({"super.invocation.invalid", "inconsistent.constructor.type"})
    @H1S1 Issue282() {}

    public class Inner {
        Inner(@H2S2 Issue282 Issue282.this) {}

        Inner(int... i) {}
    }

    public void test1() {
        // The enclosing type is @H1S1 @H2Top, the receiver type is @H1Top @H2Top
        // :: error: (enclosingexpr.type.incompatible)
        Inner inner = new Issue282().new Inner() {};
        // :: error: (enclosingexpr.type.incompatible)
        Inner inner2 = new Issue282().new Inner();
    }

    class Issue282Sub extends Issue282 {}

    public void test2() {
        // found: @H1Top @H2Top Issue282.@H1Top @H2Top Issue282Sub. required: @H1Top @H2S2 Issue282
        // :: error: (enclosingexpr.type.incompatible)
        Inner inner = new Issue282Sub().new Inner();
    }

    public static void testStatic() {
        new Issue282().new Issue282Sub() {};
    }

    class InnerGeneric<T> {
        @SuppressWarnings("unchecked")
        InnerGeneric(T... t) {}
    }

    public void test3(@H1S1 String a, @H1S1 String b, @H1S2 String c) {
        new InnerGeneric<@H1S1 String>(a, b);
        // found: @H1S2 @H2Top String. required: @H1S1 @H2Top String
        // :: error: (argument.type.incompatible)
        new InnerGeneric<@H1S1 String>(a, c);
    }
}

class Top {
    void test(@H1Top @H2S2 Issue282 outer) {
        outer.new Inner() {};
        outer.new Inner(1, 2, 3) {};
    }
}
