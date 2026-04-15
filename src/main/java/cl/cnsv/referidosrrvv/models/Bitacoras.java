/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cow
 */
@Entity
@Table(name = "RFD_BITACORAS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Bitacoras.findAll", query = "SELECT b FROM Bitacoras b"),
        @NamedQuery(name = "Bitacoras.findById", query = "SELECT b FROM Bitacoras b WHERE b.id = :id"),
        @NamedQuery(name = "Bitacoras.findByVersion", query = "SELECT b FROM Bitacoras b WHERE b.version = :version"),
        @NamedQuery(name = "Bitacoras.findByComentarios", query = "SELECT b FROM Bitacoras b WHERE b.comentarios = :comentarios"),
        @NamedQuery(name = "Bitacoras.findByFecha", query = "SELECT b FROM Bitacoras b WHERE b.fecha = :fecha"),
        @NamedQuery(name = "Bitacoras.findByUsuario", query = "SELECT b FROM Bitacoras b WHERE b.usuario = :usuario"), })
public class Bitacoras implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator2")
    @SequenceGenerator(name = "book_generator2", sequenceName = "RFD_BITACORAS_SEQ", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    private BigInteger version;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "COMENTARIOS")
    private String comentarios;

    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "USUARIO")
    private String usuario;

    @JoinColumn(name = "REFERENCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Referencias referenciaId;

    public Bitacoras() {
        // constructor vacio
    }

    public Bitacoras(BigDecimal id) {
        this.id = id;
    }

    public Bitacoras(
            BigDecimal id,
            BigInteger version,
            String comentarios,
            Date fecha,
            String usuario) {
        this.id = id;
        this.version = version;
        this.comentarios = comentarios;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Referencias getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Referencias referenciaId) {
        this.referenciaId = referenciaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bitacoras)) {
            return false;
        }
        Bitacoras other = (Bitacoras) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidosrrvv.models.Bitacoras[ id=" + id + " ]";
    }
}
