package com.kele.penetrate.page;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * 托盘程序
 */
@Slf4j
@Recognizer
@SuppressWarnings("unused")
public class Tray
{
    public static final Image icon = new ImageIcon(Objects.requireNonNull(Tray.class.getResource("/icon.png"))).getImage();

    @Autowired
    private MainFrame mainFrame;

    public Tray()
    {
        //检查系统是否支持托盘程序
        if (SystemTray.isSupported())
        {
            SystemTray tray = SystemTray.getSystemTray();
            PopupMenu popupMenu = new PopupMenu();

            //<editor-fold desc="打开">
            popupMenu.add(createMenuItem("open", e ->
            {
                if (!mainFrame.isShowing())
                {
                    mainFrame.setVisible(true);
                }
            }));
            //</editor-fold>

            //<editor-fold desc="退出">
            popupMenu.add(createMenuItem("exit", e -> System.exit(0)));
            //</editor-fold>

            //设置图标
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

    /**
     * 创建菜单
     *
     * @param label          标题
     * @param actionListener 触发的事件
     * @return MenuItem
     */
    private MenuItem createMenuItem(String label, ActionListener actionListener)
    {
        MenuItem menuItem = new MenuItem(label);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }
}
