package domain.operaciones;

public enum NombrePais {
    ARGENTINA {
        public String getNombre() {
            return "Argentina";
        }
    },
    BOLIVIA {
        public String getNombre() {
            return "Bolivia";
        }
    },
    BRASIL {
        public String getNombre() {
            return "Brasil";
        }
    },
    CHILE {
        public String getNombre() {
            return "Chile";
        }
    },
    COLOMBIA {
        public String getNombre() {
            return "Colombia";
        }
    },
    COSTA_RICA {
        public String getNombre() {
            return "Costa Rica";
        }
    },
    CUBA {
        public String getNombre() {
            return "Cuba";
        }
    },
    ECUADOR {
        public String getNombre() {
            return "Ecuador";
        }
    },
    EL_SALVADOR {
        public String getNombre() {
            return "El Salvador";
        }
    },
    GUATEMALA {
        public String getNombre() {
            return "Guatemala";
        }
    },
    HONDURAS {
        public String getNombre() {
            return "Honduras";
        }
    },
    MEXICO {
        public String getNombre() {
            return "Mexico";
        }
    },
    NICARAGUA {
        public String getNombre() {
            return "Nicaragua";
        }
    },
    PANAMA {
        public String getNombre() {
            return "Panamá";
        }
    },
    PARAGUAY {
        public String getNombre() {
            return "Paraguay";
        }
    },
    PERU {
        public String getNombre() {
            return "Peru";
        }
    },
    PUERTO_RICO {
        public String getNombre() {
            return "Puerto Rico";
        }
    },
    REPUBLICA_DOMINICANA {
        public String getNombre() {
            return "República Dominicana";
        }
    },
    URUGUAY {
        public String getNombre() {
            return "Uruguay";
        }
    },
    VENEZUELA {
        public String getNombre() {
            return "Venezuela";
        }
    };

    public abstract String getNombre();
}
/*Son los de la pagina de mercado libre https://developers.mercadolibre.com.ar/es_ar/ubicacion-y-monedas#modal2
(en el primer recurso)*/