function teste() {
    alert("work");
}


(function() {
    'use strict';

    var template = `
    <div class="hub-table-vue">
        <div class="hub-table-header mobile-hidden">
            <div v-for="cell in data.header" class="header-cell">
                <span>{{ cell }}</span>
            </div>
        </div>
        <div v-for="row in data.rows" class="hub-table-row">
            <div v-for="(cell, index) in row.cells" class="table-cell">
                <span class="label desktop-hidden">{{ data.header[index] }}</span>
                <span>{{ cell }}</span>
            </div>
        </div>
    </div>`;

    Vue.component("hub-table", {
        template: template,
        props: {
            data: {
                type: Object,
                required: true
            }
        }
    });
})();
