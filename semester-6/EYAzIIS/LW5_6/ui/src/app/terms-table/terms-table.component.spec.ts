import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TermsTableComponent } from './terms-table.component';

describe('TermsTableComponent', () => {
  let component: TermsTableComponent;
  let fixture: ComponentFixture<TermsTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TermsTableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TermsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
