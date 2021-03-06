package com.kele.penetrate.page;

import com.alibaba.fastjson.JSONObject;
import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.protocol.CancelMapping;
import com.kele.penetrate.protocol.Handshake;
import com.kele.penetrate.utils.CheckUtils;
import com.kele.penetrate.utils.FileUtils;
import com.kele.penetrate.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * 页面主入口
 */
@EqualsAndHashCode(callSuper = true)
@Recognizer
@Data
public class MainFrame extends JFrame
{
    //<editor-fold desc="属性">
    private JPanel upPanel;
    private JPanel downPanel;
    private JTextField mappingNameTextField;
    private JTextField ipTextField;
    private JTextField portTextField;
    private JRadioButton filterMappingNameRadioButton;
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

    public String getMappingName()
    {
        return mappingNameTextField.getText();
    }

    public Boolean isFilterMappingName()
    {
        return filterMappingNameRadioButton.isSelected();
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
        mappingNameTextField.setEditable(isEditable);
        portTextField.setEditable(isEditable);
        ipTextField.setEditable(isEditable);
        filterMappingNameRadioButton.setEnabled(isEditable);
        runButton.setEnabled(isEditable);
    }
    //</editor-fold>

    //<editor-fold desc="创建上面面板">
    private void createUpPanel()
    {
        Font font = new Font("宋体", Font.PLAIN, 12);

        //<editor-fold desc="系统默认配置">
        String mappingNameDefault = uuidUtils.generateShortUuid();
        boolean isFilterMappingNameDefault = true;
        String ipDefault = "127.0.0.1";
        String portDefault = "8080";
        //</editor-fold>

        //<editor-fold desc="读取本地配置">
        String readFileStr = fileUtils.readFileStr(fileUtils.rootDirectory + "/" + fileUtils.recordOperation);
        if (readFileStr != null)
        {
            try
            {
                JSONObject jsonObject = JSONObject.parseObject(readFileStr);
                if (jsonObject.containsKey("mappingName") && jsonObject.containsKey("isFilterMappingName") && jsonObject.containsKey("ip") && jsonObject.containsKey("port"))
                {
                    int confirm = JOptionPane.showConfirmDialog(null, "是否读取上次配置?", "提示", JOptionPane.YES_NO_OPTION);
                    if (confirm == 0)
                    {
                        mappingNameDefault = jsonObject.getString("mappingName");
                        isFilterMappingNameDefault = jsonObject.getBoolean("isFilterMappingName");
                        ipDefault = jsonObject.getString("ip");
                        portDefault = jsonObject.getString("port");

                    }
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

        //<editor-fold desc="映射名称">
        JLabel mappingNameLabel = new JLabel("映射名称");
        mappingNameLabel.setFont(font);
        mappingNameLabel.setBounds(10, 5, 100, 30);
        mappingNameTextField = new JTextField();
        mappingNameTextField.setBounds(80, 7, 200, 25);
        mappingNameTextField.setFont(font);
        mappingNameTextField.setText(mappingNameDefault);
        //</editor-fold>

        //<editor-fold desc="是否过滤映射名称">
        JLabel filterMappingNameLabel = new JLabel("是否过滤映射名称");
        filterMappingNameLabel.setFont(font);
        filterMappingNameLabel.setBounds(320, 5, 130, 30);
        filterMappingNameRadioButton = new JRadioButton();
        filterMappingNameRadioButton.setBounds(450, 10, 30, 20);
        filterMappingNameRadioButton.setSelected(isFilterMappingNameDefault);
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
        runButton.addActionListener(e ->
        {
            setAllEditable(false);

            String butText = runButton.getText();
            if ("启动".equals(butText))
            {
                String mappingName = getMappingName();
                String ip = getIp();
                String port = getPort();
                boolean isFilterMappingName = isFilterMappingName();
                if (!connectHandler.isConnect())
                {
                    clientLogPageManager.addLog("未与服务器链接，请稍后重试...");
                    setAllEditable(true);
                    return;
                }

                if (!checkUtils.checkMappingName(mappingName))
                {
                    clientLogPageManager.addLog("请检查映射名称是否正确(不能含有特殊字符，不能为空)");
                    setAllEditable(true);
                    return;
                }

                mappingName = mappingName.trim();
                if (mappingName.length() < 1 || mappingName.length() > 30)
                {
                    clientLogPageManager.addLog("检查映射名称长度(长度>0,长度<30)");
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
                handshake.setMappingIp(ip);
                handshake.setPort(Integer.parseInt(port));
                handshake.setFilterMappingName(isFilterMappingName);
                handshake.setMappingName(mappingName);
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
                connectHandler.send(new CancelMapping());
            }
        });
        //</editor-fold>

        upPanel.add(mappingNameLabel);
        upPanel.add(mappingNameTextField);
        upPanel.add(ipLabel);
        upPanel.add(ipTextField);
        upPanel.add(portLabel);
        upPanel.add(portTextField);
        upPanel.add(filterMappingNameLabel);
        upPanel.add(filterMappingNameRadioButton);
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
}
