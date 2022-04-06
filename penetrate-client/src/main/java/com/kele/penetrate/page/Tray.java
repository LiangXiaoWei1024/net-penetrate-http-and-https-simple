package com.kele.penetrate.page;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 托盘系统
 */
@Slf4j
@SuppressWarnings("unused")
@Recognizer
public class Tray
{
    private static final Image icon = new ImageIcon(Tray.class.getResource("/img.png")).getImage();
    @Autowired
    private MainFrame mainFrame;
    @Autowired
    private HelpFrame helpFrame;


    public Tray()
    {
        if (SystemTray.isSupported())
        {
            SystemTray tray = SystemTray.getSystemTray();
            PopupMenu popupMenu = new PopupMenu();

            //<editor-fold desc="打开">
            popupMenu.add(createMenuItem("打开", e ->
            {
                if (!mainFrame.isShowing())
                {
                    if (helpFrame.isShowing())
                    {
                        helpFrame.setVisible(false);
                    }
                    mainFrame.setVisible(true);
                }
            }));
            //</editor-fold>

            //<editor-fold desc="帮助">
            popupMenu.add(createMenuItem("帮助", e ->
            {
                if (!helpFrame.isShowing())
                {
                    if (mainFrame.isShowing())
                    {
                        mainFrame.setVisible(false);
                    }
                    helpFrame.setVisible(true);
                }
            }));
            //</editor-fold>

            //<editor-fold desc="退出">
            popupMenu.add(createMenuItem("退出", e -> System.exit(0)));
            //</editor-fold>

            TrayIcon trayIcon = new TrayIcon(icon, "penetrate", popupMenu);
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

    public MenuItem createMenuItem(String label, ActionListener actionListener)
    {
        MenuItem menuItem = new MenuItem(label);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }
}
