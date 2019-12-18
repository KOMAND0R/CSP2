import java.util.ArrayList;

public class Analyser implements Result
{

    ArrayList array = new ArrayList();

    Analyser(String input)
    {
        if (input.contains(", "))
        {
            String strArr[] = input.split(", ");
            for (int i = 0; i < strArr.length; i++)
            {
                array.add(Integer.parseInt(strArr[i]));
            }
        }
        else
        {
            array.add(Integer.parseInt(input));
        }
    }

    Analyser(int[] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            array.add(arr[i]);
        }
    }

    Analyser(ArrayList list)
    {
        array = list;
    }

    int CountEven()
    {
        int counter = 0;
        for (int i = 0; i < array.size(); i++)
        {
            if ((Integer)array.get(i) % 2 == 0)
            {
                counter++;
            }
        }
        return counter;
    }

    int CountUneven()
    {
        int counter = 0;
        for (int i = 0; i < array.size(); i++)
        {
            if ((Integer)array.get(i) % 2 == 1)
            {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public String getResult()
    {
        return "Количество чётных чисел - " + CountEven() +
                ", Количество нечётных чисел - " + CountUneven();
    }
}