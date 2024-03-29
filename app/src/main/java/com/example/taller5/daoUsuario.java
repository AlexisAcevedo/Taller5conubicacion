package com.example.taller5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class daoUsuario {
    Context c;
    Usuario u;

    ArrayList<Usuario> lista;
    SQLiteDatabase sql;

    String bd="DataBaseApp";
    String tabla="create table if not exists usuario(id integer primary key autoincrement, usuario text, password text)";


    public daoUsuario(Context c){

        this.c=c;
        sql=c.openOrCreateDatabase(bd,c.MODE_PRIVATE,null); //abre BDD
        sql.execSQL(tabla); //crear la tabla
        u= new Usuario();

    }


    public boolean insertUsuario(Usuario u){
        if(buscar(u.getUsuario())==0)
        {
            ContentValues cv= new ContentValues();
            cv.put("usuario",u.getUsuario());
            cv.put("password",u.getPassword());

            return (sql.insert("usuario",null,cv)>0);
        }else {
            return false; //no lo pudo insertar en la bdd
        }
    }


    public int buscar (String u) {
        int c = 0;
        lista = selectUsuarios();

        for (Usuario us: lista) {

            if (us.getUsuario().equals(u))
            {//para verificar que no haya otro usuario registrado de igual manera
                c++; //si hay algun usuario me va a regresar uno dentro de x
            }
        }
        return c; //si me regresa cero es que no hay usuarios registrados con ese nombre
    }


    public ArrayList<Usuario> selectUsuarios()
    { //nos retorna todos los usuarios que hay en la BDD
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        lista.clear();
        Cursor cr = sql.rawQuery("select * from usuario", null);

        if (cr != null && cr.moveToFirst())
        { //preguntamos si esta vacio

            do {//si nos devuelve al m,enos un registro
                Usuario u = new Usuario();
                u.setId(cr.getInt(0));
                u.setUsuario(cr.getString(1));
                u.setPassword(cr.getString(2));
                //extraigo

                lista.add(u);

            } while (cr.moveToNext());
        }

        return lista;

    }


    public int login (String u, String p) { //busca dentro de la BDD EL usuario y contraseña

        int c = 0;
        Cursor cr = sql.rawQuery("select * from usuario", null);

        if (cr != null && cr.moveToFirst()) { //preguntamos si esta vacio

            do {
                if (cr.getString(1).equals(u) && cr.getString(2).equals(p)) { //verifica usuario + pass
                    c++; //si lo encuentra en la BDD a =1

                }

            } while (cr.moveToNext());
        }
        return c;
    }

    public Usuario getUsuario(String u , String p){

        lista= selectUsuarios();

        for (Usuario us : lista) {
            if(us.getUsuario().equals(u)&&us.getPassword().equals(p)){
                return us;
            }
        }
        return null;
    }

    public Usuario getUsuarioById(int id){

        lista= selectUsuarios();

        for (Usuario us : lista) {
            if(us.getId()==id ){
                return us;
            }
        }
        return null;
    }

    public boolean updateUsuario (Usuario u)
    {

        ContentValues cv= new ContentValues();
        cv.put("usuario",u.getUsuario());
        cv.put("pass",u.getPassword());

        return ( sql.update("usuario",cv,"id="+u.getId(),null)>0);
    }


    public boolean deleteUsuario(int id){

        return(sql.delete("usuario","id="+id,null)>0);
    }

}

