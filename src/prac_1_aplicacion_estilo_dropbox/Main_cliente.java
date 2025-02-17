/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prac_1_aplicacion_estilo_dropbox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import prac_1_aplicacion_estilo_dropbox.Tipo_archivo;

/**
 *
 * @author eddyp
 */
public class Main_cliente extends javax.swing.JFrame {

    ArrayList<Tipo_archivo> files = new ArrayList<>();              // esto es para actualizar la ventana del directorio del servidor
    
    public Main_cliente() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        b_subir_archivos = new javax.swing.JButton();
        b_actualizar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        l_directorio = new javax.swing.JList();
        b_descargar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        b_subir_archivos.setText("Subir archivos");
        b_subir_archivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_subir_archivosActionPerformed(evt);
            }
        });

        b_actualizar.setText("ACTUALIZAR VENTANA");
        b_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_actualizarActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(l_directorio);

        b_descargar.setText("Descargar archivos");
        b_descargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_descargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_subir_archivos, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                            .addComponent(b_descargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(b_actualizar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(b_actualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(b_subir_archivos, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b_descargar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE))
                .addGap(78, 78, 78))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_subir_archivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_subir_archivosActionPerformed
        JFileChooser jf = new JFileChooser();
        //boolean hay_dir = false;
        //----------------------------------------------------------SELECCIONAR LOS ARCHIVOS A ENVIAR--1
        jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        if (!jf.isMultiSelectionEnabled()) {
           jf.setMultiSelectionEnabled(true);
        }
      
        jf.showOpenDialog(null);
        File[] files = jf.getSelectedFiles();
        
        for ( int i = 0; i<files.length; i ++){
            System.out.println("archivos: "+files[i].getAbsolutePath());
            if (files[i].isDirectory()){
                avisar_tipo_archivo(files[i], 1);   //Mande a crear la primera carpeta
                enviar_directorio(files[i]);      //Entramos al directorio y que cree otro arreglo
                //hay_dir=true;
            }else{
                //SON SOLO ARCHIVOS
                enviar_archivo(files[i]);
            }
        }
        
        System.out.println("\rFIN DE ENVIO DE ARCHIVOS");
        //----------------------------------------------------------SELECCIONAR LOS ARCHIVOS A ENVIAR--1
        this.files = solicitar_update ();
        System.out.println("Archivos en la nube: ");
        /*ta_directorio.setText("");
        for ( int i = 0; i<this.files.size(); i++){
            System.out.println("Tipo: "+this.files.get(i).getTipo_archivo()+"Nombre: "+this.files.get(i).getNombre());
            ta_directorio.append("Tipo: "+this.files.get(i).getTipo_archivo()+"\tNombre: "+this.files.get(i).getNombre()+"\n");
        }*/
        
        l_directorio.setModel(modelList(this.files));
        
    }//GEN-LAST:event_b_subir_archivosActionPerformed

    private void b_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_actualizarActionPerformed
        files = solicitar_update ();
        System.out.println("Archivos en la nube: ");
        /*ta_directorio.setText("");
        for ( int i = 0; i<files.size(); i++){
            System.out.println("Tipo: "+files.get(i).getTipo_archivo()+"Nombre: "+files.get(i).getNombre());
            ta_directorio.append("Tipo: "+files.get(i).getTipo_archivo()+"\tNombre: "+files.get(i).getNombre()+"\n");
        }*/
        
        //AQUI CREAR UN LISTMODEL
        
        l_directorio.setModel(modelList(this.files));
        
        
    }//GEN-LAST:event_b_actualizarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        files = solicitar_update ();
        System.out.println("Archivos en la nube: ");
        /*ta_directorio.setText("");
        for ( int i = 0; i<files.size(); i++){
            System.out.println("Tipo: "+files.get(i).getTipo_archivo()+"Nombre: "+files.get(i).getNombre());
            ta_directorio.append("Tipo: "+files.get(i).getTipo_archivo()+"\tNombre: "+files.get(i).getNombre()+"\n");
        }*/
        
        l_directorio.setModel(modelList(this.files));
    }//GEN-LAST:event_formWindowActivated

    private void b_descargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_descargarActionPerformed
        if (l_directorio.isSelectionEmpty()){
            JOptionPane.showMessageDialog(null, "Seleccione un directorio o archivo para ser descargado");
        }else{
            Tipo_archivo file_seleccionado = this.files.get(l_directorio.getSelectedIndex());
            
            Solicitar_descarga(file_seleccionado);
        }
    }//GEN-LAST:event_b_descargarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_cliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_actualizar;
    private javax.swing.JButton b_descargar;
    private javax.swing.JButton b_subir_archivos;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList l_directorio;
    // End of variables declaration//GEN-END:variables

    private void enviar_directorio(File dir) {
        
        File[] files = dir.listFiles();     //Leemos los archivos de la carpeta
        
        //ENVIAR QUE SE HAGA CARPETA
        
        //RECORRER EL DIRECTORIO
        for (int i = 0; i<files.length;i++){
            System.out.println("archivos: "+files[i].getAbsolutePath());
            if (files[i].isDirectory()){
                //SI ES OTRA VEZ DIRECTORIO, RECURSIVIDAD
                avisar_tipo_archivo(files[i],1);
                enviar_directorio(files[i]);      //Entramos al directorio y que cree otro arreglo
            }else{
                //ENVIAR ARCHIVOS
                enviar_archivo(files[i]);
            }
        }
        avisar_tipo_archivo(null, 0);
    }

    private void enviar_archivo(File file) {
        try{
            int pto= 9000;
            String host="127.0.0.1";
            Socket cl = new Socket(host, pto);
            
            
            File f = file;                      //obtenemos el archivo y lo guardamos en f
            String nombre = f.getName();                        //obtenemos el nombre del archivo
            long tam = f.length();                              //cuanto pesa   
            String ruta = f.getAbsolutePath();                  //donde esta ubicado
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            DataInputStream dis = new DataInputStream(new FileInputStream(ruta));
            
            dos.writeUTF("file");
            dos.flush();
            
            dos.writeUTF(nombre);
            dos.flush();
            dos.writeLong(tam);
            dos.flush();

            long enviados = 0;
            int n=0, porcentaje=0;

            while(enviados < tam){
                byte[]b = new byte[2000];
                n = dis.read(b);
                enviados = enviados+n;
                dos.write(b,0,n);
                dos.flush();
                porcentaje = (int)((enviados*100)/tam);
                System.out.println("\rEnviado el "+porcentaje+" % del archivo");
            }//while
            System.out.println("Archivo enviado");
            dis.close();
            dos.close();
            cl.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void avisar_tipo_archivo(File file, int i) {
        if (i == 1){            //abrir directorio
            try{
                
                int pto= 9000;
                String host="127.0.0.1";
                Socket cl = new Socket(host, pto);
                File dir = file;
                
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                
                dos.writeUTF("dire");
                dos.writeUTF(dir.getName());
                
                dos.close();
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (i == 0){            //cerrar directorio
            try{
                
                int pto= 9000;
                String host="127.0.0.1";
                Socket cl = new Socket(host, pto);
                File dir = file;
                
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                
                dos.writeUTF("dirs");
                
                dos.close();
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private ArrayList solicitar_update() {
        
        boolean salir=false;
        ArrayList <Tipo_archivo> files = new ArrayList<>();
        
        
        try{ 
                int pto= 9000;
                String host="127.0.0.1";
                Socket cl = new Socket(host, pto);
                
                
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                
                
                System.out.println("Solicitud de update de ventana...");
                dos.writeUTF("update");                                     //solicitamos un update al servidor
                dos.flush();
                
                while(salir!=true){                                         //aqui recibimos que tipo de archivo es y el archivo
                    
                    DataInputStream dis = new DataInputStream(cl.getInputStream());
                    String respuesta = dis.readUTF();
                    System.out.println("Cadena recibida por parte del servidor: "+respuesta);
                    
                    if (respuesta.compareToIgnoreCase("fin")==0){           //el servidor termina de enviar todos los nombres de los ficheros
                        System.out.println("Fin de update");
                        dis.close();
                        salir=true;
                        
                    }else{
                        
                        if (respuesta.compareToIgnoreCase("file")==0){      //el servidor mandara una cadena file
                            
                            System.out.println("LLego un archivo");
                            String nombre_file = dis.readUTF();
                            System.out.println("Archivo: "+nombre_file);
                            Tipo_archivo file = new Tipo_archivo("file", nombre_file);
                            files.add(file);
                            
                        }else{                                              //el servidor mandara una cadena dir
                            System.out.println("LLego un directorio");
                            String nombre_file = dis.readUTF();
                            System.out.println("Directorio: "+nombre_file);
                            Tipo_archivo dir = new Tipo_archivo("dir", nombre_file);
                            files.add(dir);
                        }
                        
                    }//else
                    
                }//while
                
                dos.close();
                     
                
        }catch(Exception e){
            e.printStackTrace();
        }//catch
        
        return files;
    }

    private DefaultListModel modelList(ArrayList <Tipo_archivo> files){
            DefaultListModel model = new DefaultListModel<>();
            
            for ( int i = 0; i<files.size();i++){
                model.addElement("Tipo: "+files.get(i).getTipo_archivo()+"     Nombre: "+files.get(i).getNombre());
            }
            
            return model;
            }

    private void Solicitar_descarga(Tipo_archivo file_seleccionado) {
        try{
            
            int pto= 9000;
                String host="127.0.0.1";
                Socket cl = new Socket(host, pto);
                
                
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                
                
                System.out.println("Solicitando descarga de: "+file_seleccionado.getNombre());
                
                dos.writeUTF("download");
                dos.flush();
                
                dos.writeUTF(file_seleccionado.getNombre());
                dos.flush();
                
                if (dis.readUTF().compareToIgnoreCase("found")==0){     //si encuentra el archivo
                    
                    System.out.println("Encontro el archivo del lado del servidor");
                    File db = new File(".");
                    db = new File(db.getPath()+"//cliente");           //DIRECTORIO RAIZ DONDE ALMACENA TODOS LOS ARCHIVOS Y CARPETAS
                    
                    DataInputStream dis_f = new DataInputStream(cl.getInputStream());     //creamos el flujo para leer el nombre del archivo
                    
                    String nombre = dis_f.readUTF();       
                    long tam = dis_f.readLong();

                    
                    System.out.println("Preparado para recibir el archivo "+nombre+" de "+tam+" bytes desde "+cl.getInetAddress()+":"+cl.getPort());
                    FileOutputStream fos_f = new FileOutputStream(db+"//"+nombre);
                    DataOutputStream dos_f = new DataOutputStream(fos_f);      //esta guarda el archivo en la dirección raiz del proyecto
                    
                    long recibidos = 0;
                    int n=0, porcentaje=0;

                    while(recibidos<tam){
                        byte[]b=new byte[2000];
                        n = dis_f.read(b);        //la b la utilizamos para guardar en el buffer lo que recibimos
                        recibidos = recibidos+n;
                        dos_f.write(b,0,n);
                        dos_f.flush();
                        porcentaje= (int)((recibidos*100)/tam);
                        System.out.println("\rRecibido el "+porcentaje+"%");
                    }//while
                    System.out.println("Archivo recibido");
                    fos_f.close();
                    dos_f.close();
                    dis_f.close();
                    
                    /*String nombre = dis.readUTF();       
                    long tam = dis.readLong();
                    File f = new File("");
                    String file = f.getAbsolutePath();
                    //System.out.println("Tipo de Dato: "+tipo_dato);
                    System.out.println("Preparado para recibir el archivo "+nombre+" de "+tam+" bytes desde "+cl.getInetAddress()+":"+cl.getPort());
                    FileOutputStream fos = new FileOutputStream(file+"//cliente//"+nombre);
                    DataOutputStream dos2 = new DataOutputStream(fos);      //esta guarda el archivo en la dirección raiz del proyecto

                    long recibidos = 0;
                    int n=0, porcentaje=0;

                    while(recibidos<tam){
                        byte[]b=new byte[2000];
                        n = dis.read(b);        //la b la utilizamos para guardar en el buffer lo que recibimos
                        recibidos = recibidos+n;
                        dos2.write(b,0,n);
                        dos2.flush();
                        porcentaje= (int)((recibidos*100)/tam);
                        System.out.println("\rRecibido el "+porcentaje+"%");
                    }//while
                    System.out.println("Archivo recibido"); 
                    
                    dos2.close();*/
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Archivo no encontrado");
                }
                
                dos.close();
                dis.close();
                cl.close();
                
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
