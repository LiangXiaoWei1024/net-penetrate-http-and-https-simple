package com.kele.penetrate.page;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;

import javax.swing.*;
import java.awt.event.*;

@Recognizer
@SuppressWarnings("unused")
public class TextAreaMenu extends JTextArea
{
    @Autowired
    private ClientLogPageManager clientLogPageManager;
    @Autowired
    private Config config;

    public TextAreaMenu()
    {
        JPopupMenu menu = new JPopupMenu();
        menu.add(createMenuItem("版本", actionEvent ->
                clientLogPageManager.addLog("当前版本：" + config.getVersion())));

        menu.add(createMenuItem("帮助", actionEvent ->
                clientLogPageManager.addLog("如有问题可以联系微信:1049705180,QQ群:704592910")));

        JMenuItem aa = createMenuItem("清空日志", actionEvent ->
                clientLogPageManager.clear());


        aa.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_MASK));


        menu.add(aa);


        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                // MouseEvent.BUTTON1:  左键点击
                // MouseEvent.BUTTON2:  中间点击(滑轮)
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    // 设置右键点击事件,打开邮件菜单
                    menu.show(TextAreaMenu.this, e.getX(), e.getY());
                }
            }
        });
    }


    /**
     * 创建菜单
     *
     * @param label          标题
     * @param actionListener 触发的事件
     * @return MenuItem
     */
    private JMenuItem createMenuItem(String label, ActionListener actionListener)
    {
        JMenuItem menu = new JMenuItem(label);
        menu.addActionListener(actionListener);
        return menu;
    }
}
