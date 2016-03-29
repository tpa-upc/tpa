package test;

import com.graphics.render.RenderStats;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by german on 28/03/2016.
 */
public class DebugGui extends JFrame {

    JTable table;

    RenderStats stats = new RenderStats();

    class MyModel implements TableModel {

        public RenderStats stats = new RenderStats();

        @Override
        public int getRowCount() {
            return 11;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) return "Property";
            else if (columnIndex == 1) return "Reported";
            else return "Meow";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return col0[rowIndex];
            switch (rowIndex) {
                case 0: return stats.vboCount+"";
                case 1: return stats.textureSwitch+"";
                case 2: return stats.shaderSwitch+"";
                case 3: return stats.fboSwitch+"";
                case 4: return stats.vertices+"";
                case 5: return "";
                case 6: return stats.allocatedTextures+"";
                case 7: return stats.allocatedVbos+"";
                case 8: return stats.allocatedEbos+"";
                case 9: return stats.allocatedFramebuffers+"";
                case 10: return stats.allocatedPrograms;
            }
            return "Meow";
        }

        String[] col0 = {"Vbo count", "Texture switches", "shader switches", "FBO switches", "Vertices", "",
        "Allocated Textures", "Allocated Vbos", "Allocated Ebos", "Allocated Framebuffers", "Allocated Programs"};
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

    MyModel model = new MyModel();
    JButton stop = new JButton("Pause");
    boolean stopped = false;

    public DebugGui () {
        table = new JTable(model);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(320, 0));
        this.add(table, BorderLayout.CENTER);
        this.add(stop, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
        table.setBackground(new Color(0xffFFFED4));//FFFED4


        stop.addActionListener(e -> {
            stopped = !stopped;
            if (stopped) stop.setText("Resume");
            else stop.setText("Pause");
            if (stopped) {
                table.setBackground(new Color(0xffffffff));//FFFED4
            } else {
                table.setBackground(new Color(0xffFFFED4));//FFFED4
            }
        });
    }

    public void doSomething(RenderStats stats) {
        if (stopped) return;
        stats.copy(model.stats);
        table.updateUI();
    }
}
