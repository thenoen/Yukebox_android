import { YukeboxClientPage } from './app.po';

describe('yukebox-client App', () => {
  let page: YukeboxClientPage;

  beforeEach(() => {
    page = new YukeboxClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
