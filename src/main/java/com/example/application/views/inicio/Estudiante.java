package com.example.application.views.inicio;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    private String id;
    private String nombre;
    private String apellido;
    private String materia;
    private List<Double> notas;

    public Estudiante() {
        this.notas = new ArrayList<>();
    }

    public Estudiante(String id, String nombre, String apellido, String materia) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.materia = materia;
        this.notas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public List<Double> getNotas() {
        return notas;
    }

    public void agregarNota(Double nota) {
        this.notas.add(nota);
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public double getPromedio() {
        if (notas.isEmpty()) {
            return 0.0;
        }
        double suma = 0.0;
        for (Double nota : notas) {
            suma += nota;
        }
        return suma / notas.size();
    }

    public int getCantidadNotas() {
        return notas.size();
    }

    public void eliminarNota(int indice) {
        if (indice >= 0 && indice < notas.size()) {
            notas.remove(indice);
        }
    }
}