package com.kele.penetrate.page;



import javax.swing.*;
import java.awt.Panel;
import java.awt.*;
import java.net.URL;


public class Panel2 extends JFrame
{

    private static JFrame frame;
    public static JPanel panel;
    public static JLabel label;

    public void createGUI()
    {
        frame = new JFrame("当前广播信号");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        BorderLayout borderLayout = new BorderLayout();  //边界布局，设置控件垂直居中

        panel = new JPanel();
        label = new JLabel("请选择广播信号");
        label.setHorizontalAlignment(SwingConstants.CENTER);  //设置控件左右居中
        panel.setLayout(borderLayout);
        panel.add(label);
        frame.add(panel);

        /*
         * 添加系统托盘
         */
        if (SystemTray.isSupported())
        {
            // 获取当前平台的系统托盘
            SystemTray tray = SystemTray.getSystemTray();

            URL url = Panel.class.getResource("/com/bp/netty/img/img.png");

            // 实例化图像对象
            ImageIcon icon = new ImageIcon(url);
            // 获得Image对象
            Image image = icon.getImage();
            // 加载一个图片用于托盘图标的显示

            // 创建点击图标时的弹出菜单
            PopupMenu popupMenu = new PopupMenu();
            MenuItem openItem = new MenuItem("打开");
            MenuItem exitItem = new MenuItem("退出");


            for (int i = 1; i < 11; i++)
            {
                MenuItem broadcastSignal = new MenuItem("广播信号" + i);
                popupMenu.add(broadcastSignal);
            }

            openItem.addActionListener(e ->
            {
                // 点击打开菜单时显示窗口
                if (!frame.isShowing())
                {
                    frame.setVisible(true);
                }
            });
            exitItem.addActionListener(e ->
            {
                // 点击退出菜单时退出程序
                System.exit(0);
            });

            popupMenu.add(openItem);
            popupMenu.add(exitItem);

            // 创建一个托盘图标
            TrayIcon trayIcon = new TrayIcon(image, "文本拷贝", popupMenu);
            // 托盘图标自适应尺寸
            trayIcon.setImageAutoSize(true);


            // 添加托盘图标到系统托盘
            try
            {
                tray.add(trayIcon);
            }
            catch (AWTException e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            System.out.println("当前系统不支持系统托盘");
        }
    }


}
