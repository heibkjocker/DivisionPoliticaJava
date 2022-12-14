/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import divisionpolitica.UtilHibernate;
import divisionpolitica.UtilIU;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import modelos.Pais;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import vistas.VistaPais;


/**
 *
 * @author Cristian Quintero
 */
public class ControladorPais {
    
    public static String rutaMapas; 
    
    private VistaPais vista;
    private List<Pais> paises;
    private String[] encabezados=new String[]{"Nombre", "Continente", "Moneda", "Tipo de region"};
    
    public ControladorPais(VistaPais vista){
        this.vista=vista;

    }
    
    public void mostrar(){
        this.vista.setTitle("Paises");
        this.vista.setLocation(50, 100);
        this.vista.setSize(600, 500);
        this.vista.setClosable(true);
        this.vista.setResizable(true);
        this.vista.setVisible(true);
        

        consultar();
        listar();
        
        this.vista.mnuMapa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarMapa();
            }
            
        });
        
        this.vista.tbl.setComponentPopupMenu(this.vista.mnuPais);
        
    }
    
    private void mostrarMapa(){
        //verificar que se haya seleccionado una fila
        if (this.vista.tbl.getSelectedRow() >= 0){
            Pais p=paises.get(this.vista.tbl.getSelectedRow());
            
            String nombreArchivo="file:///"+ rutaMapas+"/"+p.getNombre()+ ".jpg";
            try {
            byte[] bMapa=UtilIU.obtenerImagen(nombreArchivo);
            UtilIU.mostrarImagenVentana(bMapa, "Mapa de " + p.getNombre());
            } catch (Exception ex){
                UtilIU.error("Nose pudo obtener la imagen");
            }
        }
    }
    
    public void consultar(){
                //Consultar la lista de paises
        SessionFactory sf = UtilHibernate.getSessionFactory();
        Session sesion = sf.openSession();
        
        Query q = sesion.createQuery("FROM Pais");
        Iterator<Pais> it = q.iterate();
        
        paises = new ArrayList<>();
        while(it.hasNext()){
            Pais p = it.next();
            paises.add(p);
        }
    }
    
    private String[][] pasarMatriz() {
        if(paises!=null){
            String[][] datos=new String[paises.size()][encabezados.length];
            int fila=0;
            for(Pais p:paises){
                datos[fila][0]= p.getNombre();
                datos[fila][1]= p.getContinente().getNombre();
                datos[fila][2]= p.getMoneda();
                datos[fila][3]= p.getTiporegion().getTipoRegion();
                fila++;
            }
            return datos;
        }
        
        return null;
    }
    
    public void listar(){
        UtilIU.mostrarTabla(this.vista.tbl, pasarMatriz(), encabezados);
    }
    
}
