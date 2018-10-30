import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public  MainFrame() {
        init();
    }
    private void init(){
        this.setLayout(new BorderLayout(10,5)); //默认为0，0；水平间距10，垂直间距5
        ShowMemory memory=new ShowMemory();
        this.add(memory,BorderLayout.CENTER);
        this.setTitle("存储器管理");
        this.setResizable(true);
        this.setSize(1000, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
