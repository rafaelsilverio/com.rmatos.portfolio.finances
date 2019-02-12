'use strict';
var template = `
<div>
  {{ financeEndpoint }}
</div>
`;

Vue.component('finance-admin-container', {
    template: template,
    props: ['financeEndpoint']
});

new Vue({ el: '.finance-admin-container-placeholder' })
