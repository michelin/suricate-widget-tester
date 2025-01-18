import { TestBed } from '@angular/core/testing';

import { MockModule } from '../../mock.module';
import { MockedModelBuilderService } from './mocked-model-builder.service';

describe('MockUnitTestsService', () => {
  let service: MockedModelBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MockModule],
      providers: [MockedModelBuilderService]
    });

    service = TestBed.inject(MockedModelBuilderService);
  });

  it('should create', () => {
    expect(service).toBeTruthy();
  });
});
