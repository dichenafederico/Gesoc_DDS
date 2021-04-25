package domain.operaciones;

import javax.persistence.*;

@Entity
@Table(name = "Documentos_comerciales")
public class DocumentoComercial {
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo_documento")
    public TipoDocumento tipoDoc;
    @Embedded
    public Dinero montoTotal;
    @Id
    @GeneratedValue
    private Long id;

    public DocumentoComercial() {

    }

    public Dinero getMontoTotal() {
        return montoTotal;
    }
}
