package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class UDP_Client {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private final Scanner sc;
    private File file;

    public UDP_Client(String HOST, int port, String pathToLog)
    {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(HOST);
            this.port = port;
        } catch (SocketException | UnknownHostException e) { System.out.println(e.getMessage()); }
        sc = new Scanner(System.in);

        file = new File(pathToLog);
    }
    private void SendData(String data) {
        try {
            byte[] sendingData = data.getBytes();
            DatagramPacket sendingPacket = new DatagramPacket(sendingData,sendingData.length, address, port);
            socket.send(sendingPacket);
        } catch(IOException e) { System.out.println(e.getMessage()); }
    }
    private String ReceiveData() {
        String receivedString = "";
        try {
            byte[] receivedData = new byte[1024];
            DatagramPacket receivingPacket = new DatagramPacket(receivedData, receivedData.length);
            socket.receive(receivingPacket);
            receivedString = (new String(receivingPacket.getData())).trim();
        } catch(IOException e) { System.out.println(e.getMessage()); }
        WriteToFile(receivedString);
        return receivedString;
    }
    private void ShowMenu()
    {
        System.out.print("Меню:\n0) Выход\n1) Вывод массива\n2) Перезапись ячейки массива\nВвод: ");
    }
    private void ChoiceArrayMenu()
    {
        System.out.print("Выбор массива:\n1) Целочисленный\n2) Вещественный\n3) Строковый\nВвод: ");
    }
    private void ChoiceArrayCells()
    {
        System.out.println("1) Перезапись определенной ячейки\n2) Установка заданных значений нескольким ячейкам\nВвод: ");
    }
    private void WriteToFile(String data)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try
            {
                String s;
                while((s = br.readLine())!=null)
                {//построчное чтение
                    sb.append(s);
                    sb.append("\n");
                }
            }
            finally
            {
                br.close();
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException();
        }
        try
        {
            PrintWriter pw = new PrintWriter(file.getAbsoluteFile());
            try
            {
                pw.println(sb.toString() + data);
            }
            finally
            {
                pw.close();
            }
        }catch(IOException e){throw new RuntimeException();}
    }
    public void run()
    {
        SendData("Client is connected");
        System.out.println(ReceiveData());
        String choice = "";
        String choiceArray = "";
        String choiceCells = "";
        String receivedData = "";
        boolean menu = true;
        while(menu) {
            ShowMenu();
            choice = sc.nextLine();
            SendData(choice);
            switch (choice) {
                case "0" -> {
                    menu = false;
                    socket.close();
                }
                case "1" -> {
                    ChoiceArrayMenu();
                    choiceArray = sc.nextLine();
                    SendData(choiceArray);
                    receivedData = ReceiveData();
                    System.out.println(receivedData);
                }
                case "2" -> {
                    ChoiceArrayMenu();
                    choiceArray = sc.nextLine();
                    SendData(choiceArray);
                    ChoiceArrayCells();
                    choiceCells = sc.nextLine();
                    if (choiceCells.equals("1")) {
                        System.out.print("Введите номер подмассива, индекс элемента и новое значение через пробел: ");
                    } else if (choiceCells.equals("2")) {
                        System.out.print("Введите номера ячеек через запятую: ");
                    }
                    choiceCells = sc.nextLine();
                    SendData(choiceCells);
                    for (int i = 0; i < choiceArray.length(); i++) {
                        receivedData = ReceiveData();
                        System.out.println(receivedData);
                    }
                }
                default -> System.out.println("Неверный ввод");
            }
        }
    }
}