/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prac_1_aplicacion_estilo_dropbox;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;

public class Recibe {
    
    public static void main (String[] args){
        
        File db = new File(".");
        db = new File(db.getPath()+"//nube");           //DIRECTORIO RAIZ DONDE ALMACENA TODOS LOS ARCHIVOS Y CARPETAS
        
        try{
            int pto=9000;
            ServerSocket s = new ServerSocket(pto);
            System.out.println("Servicio iniciado, esperando clientes...");
            for(;;){
                Socket cl= s.accept();
                System.out.println("Cliente conectado...");
                DataInputStream dis = new DataInputStream(cl.getInputStream());     //creamos el flujo para leer el nombre del archivo
                String tipo_dato = dis.readUTF();                                   //con este leemos que tipo de dato llega
                
                if (tipo_dato.compareToIgnoreCase("update")==0){        //------------------------EL SERVIDOR MANDA LAS CARPETAS QUE CONTIENE       
                    
                    System.out.println("Entro a update");
                    
                    DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                    
                    /*dos.writeUTF("dir");
                    dos.flush();
                    dos.writeUTF("Star-Wars");
                    dos.flush();
                    dos.writeUTF("fin");
                    dos.flush();*/
                    
                    File[] files = db.listFiles();
                    for ( int i = 0; i<files.length; i ++){
                        if (files[i].isDirectory()){
                            dos.writeUTF("dir");
                            dos.flush();
                            dos.writeUTF(files[i].getName());
                            dos.flush();
                        }else{
                            dos.writeUTF("file");
                            dos.flush();
                            dos.writeUTF(files[i].getName());
                            dos.flush();
                        }
                    }
                    dos.writeUTF("fin");
                    dos.flush();
                    
                    dos.close();
                    dis.close();
                }
                
                if (tipo_dato.compareToIgnoreCase("file")==0){          //------------------------CON ESTO DETECTAMOS SI ES UN ARCHIVO
                    String nombre = dis.readUTF();       
                    long tam = dis.readLong();

                    System.out.println("Tipo de Dato: "+tipo_dato);
                    System.out.println("Preparado para recibir el archivo "+nombre+" de "+tam+" bytes desde "+cl.getInetAddress()+":"+cl.getPort());
                    FileOutputStream fos = new FileOutputStream(db+"//"+nombre);
                    DataOutputStream dos = new DataOutputStream(fos);      //esta guarda el archivo en la dirección raiz del proyecto

                    long recibidos = 0;
                    int n=0, porcentaje=0;

                    while(recibidos<tam){
                        byte[]b=new byte[2000];
                        n = dis.read(b);        //la b la utilizamos para guardar en el buffer lo que recibimos
                        recibidos = recibidos+n;
                        dos.write(b,0,n);
                        dos.flush();
                        porcentaje= (int)((recibidos*100)/tam);
                        System.out.println("\rRecibido el "+porcentaje+"%");
                    }//while
                    System.out.println("Archivo recibido");
                    fos.close();
                    dos.close();
                    dis.close();
                    
                }else{  //----------------------SI NO ES UN ARCHIVO
                    
                    if (tipo_dato.compareToIgnoreCase("dire")==0){              //AQUI ES PARA CREAR LA CARPETA
                        System.out.println("Entro a CARPETA");
                        String nom_dir = dis.readUTF();
                        System.out.println("Nombre carpeta: "+nom_dir);
                        
                        
                        File nuevo_dir = new File(db.getPath()+"//"+nom_dir);
                        nuevo_dir.mkdirs();
                        db = nuevo_dir;
                        
                            //AQUI TENGO QUE UTILIZAR MKDIRS
                        dis.close();
                        
                    }
                    
                    if (tipo_dato.compareToIgnoreCase("dirs")==0){              //AQUI ES PARA SALIR DE LA CARPETA
                        System.out.println("SALIO DE CARPETA: "+db.getPath());
                        
                        System.out.println("Regreso a directorio-> "+db.getPath());
                        File anterior_dir = new File(db.getParent());
                        db = anterior_dir;
                        
                        dis.close();
                    }
                    
                }
                
                if (tipo_dato.compareToIgnoreCase("download")==0){
                    
                    boolean se_encontro=false;
                    System.out.println("Entro a download");
                    
                    String nombre = dis.readUTF();
                    
                    DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                    
                    
                    File[] files = db.listFiles();
                    
                    for ( int i = 0; i<files.length; i ++){
                        if (files[i].getName().compareToIgnoreCase(nombre)==0){
                            System.out.println("Se encontro archivo");
                            se_encontro=true;
                            dos.writeUTF("found");
                            dos.flush();
                            
                            
                            
                            //////////////////////////////////////////////Aqui enviamos el archivo que se solicito descargar
                            File f_enviar = files[i];                                   //obtenemos el archivo del arreglo
                            String nombre_f = f_enviar.getName();                        //obtenemos el nombre del archivo
                            long tam = f_enviar.length();                              //cuanto pesa   
                            String ruta = f_enviar.getAbsolutePath();                  //donde esta ubicado
                            DataOutputStream dos_f = new DataOutputStream(cl.getOutputStream());
                            DataInputStream dis_f = new DataInputStream(new FileInputStream(ruta));
                            
                            dos_f.writeUTF(nombre_f);
                            dos_f.flush();
                            dos_f.writeLong(tam);
                            dos_f.flush();

                            long enviados = 0;
                            int n=0, porcentaje=0;
                            
                            while(enviados < tam){
                                byte[]b = new byte[2000];
                                n = dis_f.read(b);
                                enviados = enviados+n;
                                dos_f.write(b,0,n);
                                dos_f.flush();
                                porcentaje = (int)((enviados*100)/tam);
                                System.out.println("\rEnviado el "+porcentaje+" % del archivo");
                            }//while
                            System.out.println("Archivo enviado");
                            dis_f.close();
                            dos_f.close();
                            
                        break;    
                        }
                    }
                    
                    if (!se_encontro){
                        
                        System.out.println("No se encontro archivo");
                        dos.writeUTF("null");
                        dos.flush();
                            
                    }
                    
                    
                    dos.close();
                    dis.close();
                }
                
                cl.close();
            }//for
        }catch(Exception e){
            e.printStackTrace();
        }
    }//main
    
}//class
