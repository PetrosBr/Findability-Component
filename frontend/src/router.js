import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

function load(component) {
    return () => import(/* webpackPrefetch: 1, webpackChunkName: "[request]" */ `@/views/${component}.vue`);
}


const router = new Router({
    mode: 'history', // uris without hashes #, see https://router.vuejs.org/guide/essentials/history-mode.html#html5-history-mode
    linkActiveClass: 'active',
    scrollBehavior: (to, from, savedPosition) => {
        if (savedPosition) {
            return savedPosition;
        }
        if (to.hash) {
            return { selector: to.hash };
        }

        return { x: 0, y: 0 };
    },
    routes: [
    	{
            path: '/',
            name: 'Home',
            component: load('home/Home'),
            meta: { title: 'Home', icon: 'lnr-home' },
        },
        //{
            //path: '/data-check-in',
            //name: 'data-check-in',
            //component: load('data-checkin/DataCheckIn'),
            //meta: { title: 'Data Check-in', icon: 'lnr-upload' },
        //},
        {
            path: '/findability-component',
            name: 'FindabilityComponent',
            component: load('findability-component/FindabilityComponent'),
            meta: { title: 'Findability Component', icon: 'lnr-car' },
        },
        {
            path: '/500',
            name: 'error500',
            component: load('Error500'),
            meta: { title: '500' },
        },
        {
            path: '*',
            name: 'error404',
            component: load('Error404'),
            meta: { title: '404' },
        }    	
    ]
});

//Adjust window title, based on route title
router.beforeEach((to, from, next) => {
    if (document) {
        const title = to.meta.title || to.name;
        document.title = `MOBISPACES - ${title}`;
    }
    return next();
});

export default router;