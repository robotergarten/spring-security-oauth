import {Component} from '@angular/core';
 
@Component({
    selector: 'app-root',
    template: `<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="/">Retail Compliance Report Service</a>
    </div>
  </div>
</nav>
<router-outlet></router-outlet>`
})

export class AppComponent {}
