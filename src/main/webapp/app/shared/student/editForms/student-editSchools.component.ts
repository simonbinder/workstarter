import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';


@Component({
  selector: 'student-editSchools',
  templateUrl: './student-editSchools.component.html',
  styleUrls: [
        '../../editView/editView.scss'
    ]
})

export class StudentEditSchools implements OnInit {
  position: string;
  careerlevel: string;
  describtion: string;
  companyname: string;
  companydomain: string;
  companyindustry: string;
  companylocation: string;

  retProfession:object;

  constructor(
    private languageService: JhiLanguageService,
    
  ) 
  {
  }

  ngOnInit() {
    this.languageService.addLocation('editView');
    
  }

  save()
  {
      this.retProfession = 
      {
        position: this.position,
        careerlevel: this.careerlevel,
        describtion: this.describtion,
        companyname: this.companyname,
        companydomain: this.companydomain,
        companyindustry: this.companyindustry,
        companylocation: this.companylocation
      }

      
    
  }

}