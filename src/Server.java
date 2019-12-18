import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Server
{
    public static void main(String[] args) throws Throwable
    {
        ServerSocket ss = new ServerSocket(8080);
        while (true)
        {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }
}

class SocketProcessor implements Runnable
{
    private Socket s;
    private InputStream is;
    private OutputStream os;

    protected SocketProcessor(Socket s) throws Throwable
    {
        this.s = s;
        this.is = s.getInputStream();
        this.os = s.getOutputStream();
    }

    @Override
    public void run()
    {
        try
        {
            String request = readInputHeaders();
            String input = request.split("/")[1];
            input = input.substring(0, input.length() - 5);
            writeResponse(createHTML(input, input));
        }
        catch (Throwable t)
        {
            System.out.println(t);
        }
        finally
        {
            try
            {
                s.close();
            }
            catch (Throwable t)
            {
                System.out.println(t);
            }
        }
        System.err.println("Client processing finished");
    }


    private void writeResponse(String s) throws Throwable
    {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: LabWork2\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + s.getBytes().length + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + s;
        os.write(result.getBytes());
        os.flush();
        os.close();
    }

    boolean fullRequest = false;

    private String readInputHeaders() throws IOException
    {
        String str = "", value = "";
        if (!fullRequest)
        {
            boolean first = false;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (((str = br.readLine()) != null) && (str.trim().length() != 0))
            {
                str = URLDecoder.decode(str, StandardCharsets.UTF_8.name());

                if (!first)
                    value = str;
                first = true;
            }
            fullRequest = true;
        }
        return value;
    }

    private static String createHTML(String request, String inputString)
    {
        Analyser analyser = new Analyser(inputString);
        String HTML =
                "<html>" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "</head>" +
                        "<body>" +
                        "<h2>Работу выполнил: Бедриков Артём Александрович</h2>" +
                        "<h3>Номер группы: ИКБО-02-17</h3>" +
                        "<h3>Номер индивидуального задания: 4</h3>" +
                        "<p>Текст индивидуального задания: Подсчет четных и нечетных символов. Числа должны поступать в виде строки с некоторым разделителем " +
                        "(пример: «11, 32, 1, 22, 14»); в массиве; списком чисел.</p>" +
                        "<br>" +
                        "<h3>Запрос: " +
                        request +
                        "</h3>" +
                        "<p>Ответ: " +
                        analyser.getResult() +
                        "</p>" +
                        "</body>" +
                        "</html>";
        return HTML;
    }
}

