import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubkidditComponent } from './subkiddit.component';

describe('SubkidditComponent', () => {
  let component: SubkidditComponent;
  let fixture: ComponentFixture<SubkidditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubkidditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubkidditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
