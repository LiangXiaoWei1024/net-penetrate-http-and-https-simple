package com.kele.penetrate.page;


import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

@Slf4j
public class Panel
{

    private JPanel jPanel;
    private JFrame frame;


    private void createFrame()
    {
        frame = new JFrame("penetrate");
        frame.setLayout(null);
        frame.setSize(500, 350);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        JPanel panelTop = new JPanel();
        panelTop.setSize(500, 100);//jpanel的大小   [宽,高]
        panelTop.setLocation(0, 0);//jpanel的位置  [左顶点的坐标]

        panelTop.setPreferredSize(new Dimension(100, 100));//关键代码,设置JPanel的大小
        Color bgColor = new Color(255, 255, 255);
        panelTop.setBackground(bgColor);
        frame.add(panelTop);


        JPanel a = new JPanel();
        a.setSize(500, 250);//jpanel的大小   [宽,高]
        a.setLocation(0, 100);//jpanel的位置  [左顶点的坐标]

        a.setPreferredSize(new Dimension(100, 100));//关键代码,设置JPanel的大小
        Color aa = new Color(44, 44, 44);
        a.setBackground(aa);
        frame.add(a);


        frame.setVisible(true);
    }


    public void createGUI()
    {
        createFrame();

        if (SystemTray.isSupported())
        {
            SystemTray tray = SystemTray.getSystemTray();
            URL url = Panel.class.getResource("/img.png");
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage();

            PopupMenu popupMenu = new PopupMenu();
            MenuItem openItem = new MenuItem("open");
            MenuItem exitItem = new MenuItem("exit");

            openItem.addActionListener(e ->
            {
                if (!frame.isShowing())
                {
                    frame.setVisible(true);
                }
            });
            exitItem.addActionListener(e -> System.exit(0));

            popupMenu.add(openItem);
            popupMenu.add(exitItem);

            TrayIcon trayIcon = new TrayIcon(image, "penetrate", popupMenu);
            trayIcon.setImageAutoSize(true);
            try
            {
                tray.add(trayIcon);
            }
            catch (AWTException e)
            {
                log.error("图标添加失败");
            }
        }
        else
        {
            log.error("当前系统不支持托盘系统");
        }

    }


}
