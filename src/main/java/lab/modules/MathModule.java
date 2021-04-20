package lab.modules;
import lab.interfaces.IFunc;
import lab.interfaces.ISysFunc;
import lab.models.Point;
import lab.models.Result;
import lab.models.ResultSetForSys;
import java.util.ArrayList;
import java.util.Scanner;

public class MathModule {
    public static void execute(IFunc func, IFunc dfunc) {
        //Для нелинейных уравнений
        Scanner scanner = new Scanner(System.in);
        double left, right, eps;
        while (true) {
            try {
                System.out.println("Введите точность:");
                eps = Double.parseDouble(scanner.nextLine());
                System.out.println("Введите левую границу:");
                left = Double.parseDouble(scanner.nextLine());
                System.out.println("Введите правую границу:");
                right = Double.parseDouble(scanner.nextLine());
                if (eps <= 0 || eps >= 1) {
                    System.out.println("Точность должна быть больше 0 и меньше 1.");
                } else {
                    break;
                }
            }
            catch (Exception ignored){}
        }

        ArrayList<Point> points = new ArrayList<>();
        Result result1 = MathModule.iterMethod(func, eps);
        Result result2 = MathModule.tangentMethod(func, dfunc, left, right, eps);

        System.out.println("Результат метода простых итераций: " + result1.getX());
        System.out.println("Время выполнения: " + result1.getTime());
        System.out.println("Количество итераций: " + result1.getIter());
        System.out.println("Результат метода касательных: " + result2.getX());
        System.out.println("Время выполнения: " + result2.getTime());
        System.out.println("Количество итераций: " + result2.getIter());
        System.out.println("Разница во времени: " + Math.abs(result1.getTime()-result2.getTime()));

        points.add(new Point(result1.getX(), 0));
        points.add(new Point(result2.getX(), 0));

        new GraphModule(func, points);
    }
    public static void execute(ISysFunc func) {
        // для систем нелинейных уравнений
        Scanner scanner = new Scanner(System.in);
        double eps, x, y;
        while (true) {
            System.out.println("Введите приближение x:");
            x = Double.parseDouble(scanner.nextLine());
            System.out.println("Введите приближение y:");
            y = Double.parseDouble(scanner.nextLine());
            System.out.println("Введите точность:");
            eps = Double.parseDouble(scanner.nextLine());
            if (eps <= 0 || eps >= 1) {
                System.out.println("Точность должна быть больше 0 и меньше 1.");
            }
            else{
                break;
            }
        }
        ArrayList<Point> points = new ArrayList<>();
        ResultSetForSys result = MathModule.iterMethod(func, x, y, eps);
        points.add(result.getPoint());
        result.print();
        new GraphModule(func.getDraw(), points);
    }



    /*
    метод простых итераций предполагает схему
    xk = g(xk-1) где g(x) = x + b*f(x) где b - произвольное число
    */
    static double g(double x, IFunc f)
    {
        return x + 0.1*f.solve(x);
    }
    public static Result iterMethod(IFunc func, double eps) {
        Result result = new Result();
        long start = System.currentTimeMillis();
        double x = 1;
        int iter = 1;
        while (eps < Math.abs(func.solve(x))) {
            x = g(x, func);
            iter++;
        }
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        result.setX(x);
        result.setIter(iter);
        result.setTime(timeConsumedMillis);
        return result;
    }

    public static Result tangentMethod(IFunc function, IFunc dfunction, double left, double right, double eps) {
        Result result = new Result();
        long start = System.currentTimeMillis();
        double x;
        if (function.solve(left)*dfunction.solve(left)<0) {
            x = left;
        }
        else {
            x = right;
        }
        double counter = Math.abs(dfunction.solve(x));
        int n=0;
        while  (counter > eps)
        {
            x = x - (function.solve(x) / dfunction.solve(x));
            n += 1;
            counter--;
        }
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        result.setX(x);
        result.setIter(n);
        result.setTime(timeConsumedMillis);
        return result;
    }

    public static ResultSetForSys iterMethod(ISysFunc func, double x, double y, double eps) {
        ResultSetForSys result = new ResultSetForSys();
        double x0 = x, y0 = y, d1, d2;
        int i = 1;
        do {
            x = func.g_x(y0);
            y = func.g_y(x0);
            d1 = func.f1(x, y);
            d2 = func.f2(x, y);
            x0 = x;
            y0 = y;
            result.addIter(x, y, Math.abs(d1), Math.abs(d2));
        } while (Math.abs(d1) > eps || Math.abs(d2) > eps);
        result.setPoint(new Point(x, y));
        return result;
    }

    public static boolean pointChecker(double left, double right, double point) {
        return point >= left && point <= right;
    }
}
