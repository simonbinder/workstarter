import { Component, OnInit } from '@angular/core';


@Component({
    moduleId: module.id +"",
    selector: 'cv',
    templateUrl: './cv.component.html',
    styleUrls: [
        'cv.css'
    ]
})

export class CvComponent {
    contactTel: String;
    contactMes: String;

    image: String;
    name = "Vorname"
    lastname: "Nachname";
    status: String;

    spruch: String;

    jobs: String[];
    education: String[];
    searches: String[];
    gives: String[];


    constructor()
    {
        status = "BWL Studentin in Hohenheim";
        this.gives = ['Coolness', 'Freundlichkeit', 'Erfahrung', 'Coolness', 'Freundlichkeit', 'Erfahrung', 'Coolness', 'Freundlichkeit', 'Erfahrung', 'Freundlichkeit', 'Erfahrung'];
        this.searches = ['Fussball', 'Geld', 'Kontakte'];
    }
}