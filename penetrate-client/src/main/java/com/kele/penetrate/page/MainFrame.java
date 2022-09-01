package com.kele.penetrate.page;

import com.alibaba.fastjson.JSONObject;
import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.protocol.Cancel;
import com.kele.penetrate.protocol.Handshake;
import com.kele.penetrate.utils.CheckUtils;
import com.kele.penetrate.utils.FileUtils;
import com.kele.penetrate.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * 页面主入口
 */
@EqualsAndHashCode(callSuper = true)
@Recognizer
@Data
@SuppressWarnings("unused")
public class MainFrame extends JFrame
{
    //<editor-fold desc="属性">
    private JPanel upPanel;
    private JPanel downPanel;
    private JTextField customDomainNameTextField;
    private JTextField ipTextField;
    private JTextField portTextField;
    private JRadioButton autoStartRadioButton;
    @Autowired
    private TextAreaMenu logTextArea;
    private JButton runButton;
    private JScrollPane logTextAreaScrollPane;
    private int changeWidth;
    private int changeHeight;
    //</editor-fold>

    //<editor-fold desc="注入">
    @Autowired
    private UUIDUtils uuidUtils;
    @Autowired
    private Config config;
    @Autowired
    private ClientLogPageManager clientLogPageManager;
    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private CheckUtils checkUtils;
    @Autowired
    private FileUtils fileUtils;
    //</editor-fold>

    //<editor-fold desc="构造">
    public MainFrame() throws HeadlessException
    {
        //<editor-fold desc="判断系统修改页面尺寸">
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            changeWidth = 16;
            changeHeight = 6;
        }
        //</editor-fold>

        setTitle("penetrate(大家可以加一下交流QQ群:704592910)");
        setLayout(null);
        setSize(530 + changeWidth, 400 + changeHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setIconImage(Tray.icon);
    }
    //</editor-fold>

    //<editor-fold desc="初始化">
    public void init()
    {
        createUpPanel();
        createDownPanel();
        setVisible(true);
    }
    //</editor-fold>

    //<editor-fold desc="对属性操作的方法">
    public String getIp()
    {
        return ipTextField.getText();
    }

    public String getPort()
    {
        return portTextField.getText();
    }

    public String getCustomDomainName()
    {
        return customDomainNameTextField.getText();
    }

    public Boolean isAutoStart()
    {
        return autoStartRadioButton.isSelected();
    }

    public void setLogTextArea(List<ClientLogPageManager.LogInfo> logList)
    {
        StringBuilder builder = new StringBuilder();
        for (ClientLogPageManager.LogInfo logInfo : logList)
        {
            builder.append(logInfo.getTime()).append(" - ").append(logInfo.getMsg()).append("\r\n");
        }
        logTextArea.setText(builder.toString());
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }
    //</editor-fold>

    //<editor-fold desc="设置是否可以编辑">
    public void setAllEditable(boolean isEditable)
    {
        customDomainNameTextField.setEditable(isEditable);
        portTextField.setEditable(isEditable);
        ipTextField.setEditable(isEditable);
        autoStartRadioButton.setEnabled(isEditable);
        runButton.setEnabled(isEditable);
    }
    //</editor-fold>

    //<editor-fold desc="创建上面面板">
    private void createUpPanel()
    {
        Font font = new Font("宋体", Font.PLAIN, 12);

        //<editor-fold desc="系统默认配置">
        String customDomainName = uuidUtils.generateShortUUID();
        boolean isAutoStart = false;
        String ipDefault = "127.0.0.1";
        String portDefault = "8080";
        //</editor-fold>

        //<editor-fold desc="读取本地配置">
        String recordOperationStr = fileUtils.readFileStr(fileUtils.rootDirectory + "/" + fileUtils.recordOperation);
        if (recordOperationStr != null)
        {
            try
            {
                JSONObject recordOperationJson = JSONObject.parseObject(recordOperationStr);
                if (recordOperationJson.containsKey("customDomainName") && recordOperationJson.containsKey("isAutoStart") && recordOperationJson.containsKey("ip") && recordOperationJson.containsKey("port"))
                {
                    customDomainName = recordOperationJson.getString("customDomainName");
                    isAutoStart = recordOperationJson.getBoolean("isAutoStart");
                    ipDefault = recordOperationJson.getString("ip");
                    portDefault = recordOperationJson.getString("port");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        //</editor-fold>

        //<editor-fold desc="面板">
        upPanel = new JPanel();
        upPanel.setLayout(null);
        upPanel.setBounds(15, 15, 500, 120);
        upPanel.setBackground(new Color(255, 255, 255));
        //</editor-fold>

        //<editor-fold desc="二级域名">
        JLabel customDomainNameLabel = new JLabel("二级域名");
        customDomainNameLabel.setFont(font);
        customDomainNameLabel.setBounds(10, 5, 100, 30);

        customDomainNameTextField = new JTextField();
        customDomainNameTextField.setBounds(80, 7, 140, 25);
        customDomainNameTextField.setFont(font);
        customDomainNameTextField.setText(customDomainName);

        JLabel customDomainNameLastLabel = new JLabel(config.getDomainName());
        customDomainNameLastLabel.setFont(font);
        customDomainNameLastLabel.setBounds(220, 5, 100, 30);
        //</editor-fold>

        //<editor-fold desc="自动启动">
        JLabel autoStartLabel = new JLabel("自动启动");
        autoStartLabel.setFont(font);
        autoStartLabel.setBounds(320, 5, 50, 30);

        autoStartRadioButton = new JRadioButton();
        autoStartRadioButton.setBounds(400, 10, 30, 20);
        autoStartRadioButton.setSelected(isAutoStart);
        //</editor-fold>

        //<editor-fold desc="IP">
        JLabel ipLabel = new JLabel("IP");
        ipLabel.setFont(font);
        ipLabel.setBounds(10, 45, 50, 30);
        ipTextField = new JTextField();
        ipTextField.setBounds(80, 47, 200, 25);
        ipTextField.setFont(font);
        ipTextField.setText(ipDefault);
        //</editor-fold>

        //<editor-fold desc="端口">
        JLabel portLabel = new JLabel("端口");
        portLabel.setFont(font);
        portLabel.setBounds(320, 45, 50, 30);
        portTextField = new JTextField();
        portTextField.setBounds(360, 47, 130, 25);
        portTextField.setFont(font);
        portTextField.setText(portDefault);
        //</editor-fold>

        //<editor-fold desc="启动">
        runButton = new JButton("启动");
        runButton.setFont(font);
        runButton.setBounds(200, 85, 100, 25);
        runButton.addActionListener(this::startButtonClickHandle);
        //</editor-fold>

        upPanel.add(customDomainNameLabel);
        upPanel.add(customDomainNameTextField);
        upPanel.add(customDomainNameLastLabel);
        upPanel.add(ipLabel);
        upPanel.add(ipTextField);
        upPanel.add(portLabel);
        upPanel.add(portTextField);
        upPanel.add(autoStartLabel);
        upPanel.add(autoStartRadioButton);
        upPanel.add(runButton);
        upPanel.setFocusable(true);

        this.add(upPanel);
    }
    //</editor-fold>

    //<editor-fold desc="创建下面面板">
    private void createDownPanel()
    {
        downPanel = new JPanel();
        downPanel.setLayout(null);
        downPanel.setBounds(15, 135, 500, 220);
        downPanel.setLocation(15, 135);
        downPanel.setBackground(new Color(44, 44, 44));


        logTextArea.setForeground(new Color(255, 255, 255));
        logTextArea.setBackground(new Color(44, 44, 44));
        logTextArea.setFont(new Font("宋体", Font.PLAIN, 10));
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setEditable(false);


        logTextAreaScrollPane = new JScrollPane(logTextArea);
        logTextAreaScrollPane.setBounds(10, 5, 480, 210);
        logTextAreaScrollPane.setBackground(new Color(44, 44, 44));
        logTextAreaScrollPane.setBorder(new LineBorder(null, 0));

        downPanel.add(logTextAreaScrollPane);
        this.add(downPanel);
    }
    //</editor-fold>

    //<editor-fold desc="启动点击处理">
    public void startButtonClickHandle(ActionEvent event)
    {
        setAllEditable(false);
        String butText = runButton.getText();

        if ("启动".equals(butText))
        {
            String customDomainName = getCustomDomainName();
            String ip = getIp();
            String port = getPort();
            boolean isAutoStart = isAutoStart();
            if (!connectHandler.isConnect())
            {
                clientLogPageManager.addLog("未与服务器链接，请稍后重试...");
                setAllEditable(true);
                return;
            }

            if (!checkUtils.checkDomainName(customDomainName))
            {
                clientLogPageManager.addLog("请二级域名是否正确(不能含有特殊字符(不包含.)，不能为空)");
                setAllEditable(true);
                return;
            }

            customDomainName = customDomainName.trim();
            if (customDomainName.length() < 1 || customDomainName.length() > 30)
            {
                clientLogPageManager.addLog("检查二级域名长度(长度>0,长度<=30)");
                setAllEditable(true);
                return;
            }

            if (!checkUtils.checkPort(port))
            {
                clientLogPageManager.addLog("请检查端口是否正确");
                setAllEditable(true);
                return;
            }

            port = port.trim();
            if (port.length() < 1 || port.length() > 9)
            {
                clientLogPageManager.addLog("检查端口长度(长度>0,长度<9)");
                setAllEditable(true);
                return;
            }

            if (!checkUtils.checkIp(ip))
            {
                clientLogPageManager.addLog("ip地址不能为空");
                setAllEditable(true);
                return;
            }

            ip = ip.trim();
            if (ip.length() < 1 || ip.length() > 50)
            {
                clientLogPageManager.addLog("检查IP长度(长度>0,长度<=50)");
                setAllEditable(true);
                return;
            }

            Handshake handshake = new Handshake();
            handshake.setVersion(config.getVersion());
            handshake.setCustomDomainName(customDomainName + config.getDomainName());
            connectHandler.setHandshake(handshake);
            connectHandler.send(handshake);
            clientLogPageManager.addLog("发送启动请求");
        }
        else
        {
            setAllEditable(false);
            if (connectHandler.getChannel() == null || !connectHandler.getChannel().isActive())
            {
                clientLogPageManager.addLog("未与服务器链接，请稍后重试...");
                runButton.setEnabled(true);
                return;
            }
            clientLogPageManager.addLog("发送暂停请求");
            connectHandler.setHandshake(null);
            connectHandler.send(new Cancel());
        }
    }
    //</editor-fold>
}
