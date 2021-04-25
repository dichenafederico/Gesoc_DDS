package domain.operaciones;

public enum NombreDivisa {
    BALBOA {
        public String getNombre() {
            return "Balboa";
        }
    },
    BOLIVAR_FUERTE {
        public String getNombre() {
            return "Bolivar fuerte";
        }
    },
    BOLIVIANO {
        public String getNombre() {
            return "Boliviano";
        }
    },
    COLONES {
        public String getNombre() {
            return "Colones";
        }
    },
    CORDOBA {
        public String getNombre() {
            return "Córdoba";
        }
    },
    DOLAR {
        public String getNombre() {
            return "Dólar";
        }
    },
    EURO {
        public String getNombre() {
            return "Euro";
        }
    },
    GUARANI {
        public String getNombre() {
            return "Guaraní";
        }
    },
    LEMPIRA {
        public String getNombre() {
            return "Lempira";
        }
    },
    PESO_ARGENTINO {
        public String getNombre() {
            return "Peso argentino";
        }
    },
    PESO_CHILENO {
        public String getNombre() {
            return "Peso Chileno";
        }
    },
    PESO_COLOMBIANO {
        public String getNombre() {
            return "Peso colombiano";
        }
    },
    PESO_CUBANO_CONVERTIBLE {
        public String getNombre() {
            return "Peso Cubano Convertible";
        }
    },
    PESO_DOMINICANO {
        public String getNombre() {
            return "Peso Dominicano";
        }
    },
    PESO_MEXICANO {
        public String getNombre() {
            return "Peso Mexicano";
        }
    },
    PESO_URUGUAYO {
        public String getNombre() {
            return "Peso Uruguayo";
        }
    },
    QUETZAL_GUATEMALTECO {
        public String getNombre() {
            return "Quetzal Guatemalteco";
        }
    },
    REAL {
        public String getNombre() {
            return "Real";
        }
    },
    SOLES {
        public String getNombre() {
            return "Soles";
        }
    },
    UNIDAD_DE_FOMENTO {
        public String getNombre() {
            return "Unidad de Fomento";
        }
    };

    public abstract String getNombre();
}

/*Son los de la pagina de mercado libre https://developers.mercadolibre.com.ar/es_ar/ubicacion-y-monedas#modal2
(en el quinto recurso)*/