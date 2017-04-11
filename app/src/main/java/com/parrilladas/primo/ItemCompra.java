package com.parrilladas.primo;
/**
 *@autor   jhony Guaman & John Morrillo
 *@date       8-febrero-2016
 * Clase ItemCompra
 */

public class ItemCompra {
    // variable de la entidad mesas
	protected long id,id_pedido;
    protected int est,idxcantidad,stock,idusuario,cant=1;
    protected String nombre_mesa,preciostatico,nombre_secos,precio,estado_pedido,detalle;
    protected double precio_secos;
    protected double total_pedido;

    // Metodos get's
    public String getNombre_secos() {
        return nombre_secos;
    }
    public String getNombre_mesa() {return nombre_mesa;}
    public String getPreciostatico() {
        return preciostatico;
    }
    public long getId() {
        return id;
    }
    public int getEst() {
        return est;
    }
    public int getCant() {
        return cant;
    }
    public double getPrecio_secos() {
        return precio_secos;
    }
    public int getStock() {
        return stock;
    }
    public int getIdxcantidad() {  return idxcantidad; }
    public long getId_pedido() {
        return id_pedido;
    }
    public String getEstado_pedido() {
        return estado_pedido;
    }
    public double getTotal_pedido() {
        return total_pedido;
    }
    public int getIdusuario() {
        return idusuario;
    }
    public String getDetalle() {
        return detalle;
    }

    //Metodos set's
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
    public void setNombre_secos(String nombre_secos) {
        this.nombre_secos = nombre_secos;
    }
    public void setPrecio_secos(double precio_secos) {
        this.precio_secos = precio_secos;
    }
    public void setPreciostatico(String preciostatico) {
        this.preciostatico = preciostatico;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setNombre_mesa(String nombre_mesa) {
        this.nombre_mesa = nombre_mesa;
    }
    public void setEst(int est) {
        this.est = est;
    }
    public void setCant(int cant) {
        this.cant = cant;
    }
    public void setStock(int stock) { this.stock = stock;    }
    public void setIdxcantidad(int idxcantidad) {        this.idxcantidad = idxcantidad;    }
    public void setId_pedido(long id_pedido) {
        this.id_pedido = id_pedido;
    }
    public void setEstado_pedido(String estado_pedido) {
        this.estado_pedido = estado_pedido;
    }
    public void setTotal_pedido(double total_pedido) {
        this.total_pedido = total_pedido;
    }
    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }


    //Contructores de la clase itemCompra
    public ItemCompra(String nombre_mesa, int estado, int idusuario) {
        this.nombre_mesa = nombre_mesa;
        this.est = estado;
        this.idusuario =  idusuario;
    }

        /*lista para cargar los productos*/
    public ItemCompra(long id, String nombre_secos, double precio_secos, int stock, int idxcantidad){
        this.id = id;
        this.nombre_secos=nombre_secos;
        this.precio_secos=precio_secos;
        this.stock=stock;
        this.idxcantidad=idxcantidad;
    }

    public ItemCompra(long id, String nombre, String precio, int cant, String preciostatico, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio=precio;
        this.cant=cant;
        this.preciostatico=preciostatico;
        this.stock=stock;
    }
    public ItemCompra(long id, String nombre, String precio, int cant, String preciostatico) {
        this.id = id;
        this.nombre = nombre;
        this.precio=precio;
        this.cant=cant;
        this.preciostatico=preciostatico;

    }

    /*itemcompra para llenar el reporte de ventas del mesero*/
    public ItemCompra(long id_pedido, double total_pedido, String estado_pedido){
        this.id_pedido=id_pedido;
        this.total_pedido=total_pedido;
        this.estado_pedido=estado_pedido;
    }



























    protected String rutaImagen;

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}
	protected String nombre;

	/*protected String precio;*/

    public String getPreciop() {
        return preciop;
    }

    public void setPreciop(String preciop) {
        this.preciop = preciop;
    }

    protected String id_sec;
    protected String preciop;
    protected String fecha_incio;
    protected String fecha_final;
    protected String ruc;
    protected String seccion;

    protected String tipo;
    protected String descripcion;
    protected  int estado;

    protected  long idH;

    public long getIdH() {
        return idH;
    }

    public void setIdH(long idH) {
        this.idH = idH;
    }

    public String getNombreH() {
        return nombreH;
    }

    public void setNombreH(String nombreH) {
        this.nombreH = nombreH;
    }

    public String getFechaH() {
        return fechaH;
    }

    public void setFechaH(String fechaH) {
        this.fechaH = fechaH;
    }

    public String getEstadoH() {
        return EstadoH;
    }

    public void setEstadoH(String estadoH) {
        EstadoH = estadoH;
    }

    public String getValorH() {
        return valorH;
    }

    public void setValorH(String valorH) {
        this.valorH = valorH;
    }

    protected String nombreH;
    protected String fechaH;
    protected String valorH;
    protected  String EstadoH;

    public int getVisto() {
        return visto;
    }

    public void setVisto(int visto) {
        this.visto = visto;
    }

    public String getId_sec() {
        return id_sec;
    }

    public void setId_sec(String id_sec) {
        this.id_sec = id_sec;
    }

    protected int visto;
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }




    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }



    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }



    public String getFecha_incio() {
        return fecha_incio;
    }

    public void setFecha_incio(String fecha_incio) {
        this.fecha_incio = fecha_incio;
    }




	

	private boolean seleccionado=false;
	public ItemCompra() {
		this.nombre = "";
		this.tipo = "";
		//this.rutaImagen = "";
		this.precio="";
		
	}

	
	public ItemCompra(long id, String nombre, String precio, String fecha_incio, String fecha_final, String rutaImagen) {
		this.id = id;
		this.nombre = nombre;
	//	this.tipo = tipo;
		//this.rutaImagen = "";
		this.precio=precio;
        this.fecha_incio=fecha_incio;
        this.fecha_final = fecha_final;
        this.rutaImagen = rutaImagen;
	}

    public ItemCompra(long id, String ruc, String descripcion, String preciop, String tipo, String detalle, String id_sec, String rutaImagen) {
        this.id = id;
        this.ruc=ruc;
        this.descripcion=descripcion;
        this.preciop=preciop;
        this.tipo=tipo;
        this.detalle=detalle;
        this.id_sec=id_sec;
        this.rutaImagen = rutaImagen;
       // this.visto=visto;
    }

    public ItemCompra(long idH, String nombreH, String fechaH, String valorH, String EstadoH) {
        this.idH = idH;
        this.nombreH = nombreH;
        this.fechaH =fechaH;
        this.valorH = valorH;
        this.EstadoH = EstadoH;
        // this.visto=visto;
    }
    public ItemCompra(long id, String ruc, String descripcion, String preciop, String tipo, String detalle, String id_sec, String rutaImagen, int visto) {
        this.id = id;
        this.ruc=ruc;
        this.descripcion=descripcion;
        this.preciop=preciop;
        this.tipo=tipo;
        this.detalle=detalle;
        this.id_sec=id_sec;
        this.rutaImagen = rutaImagen;
        this.visto=visto;
    }



    public ItemCompra(long id, String nombre, String tipo, String precio) {
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.rutaImagen = rutaImagen;
		this.precio=precio;
	}

    public ItemCompra(int id, String ruc, String seccion, int estado) {
        this.id = id;
        this.ruc=ruc;
        this.seccion=seccion;
        this.estado=estado;
    }
	


	
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getPrecio() {
		return precio;
	}
	
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}



}

