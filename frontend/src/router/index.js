import {createRouter, createWebHistory} from 'vue-router'
import MainPage from "@/pages/MainPage.vue";
import CreateOrganizationPage from "@/pages/CreateOrganizationPage.vue";
import AllOrganizationsPage from "@/pages/AllOrganizationsPage.vue";
import EachOrganizationPage from "@/pages/EachOrganizationPage.vue";
import LoginPage from "@/pages/LoginPage.vue";
import RegistrationPage from "@/pages/RegistrationPage.vue";
import AccountPage from "@/pages/AccountPage.vue";
import store from "@/store/index"
import AddWorkerPage from "@/pages/AddWorkerPage.vue";
import EachWorkerPage from "@/pages/EachWorkerPage.vue";
import EachTaskBoardPage from "@/pages/EachTaskBoardPage.vue";
import EachTaskPage from "@/pages/EachTaskPage.vue";
import {nextTick} from "vue";
import AccountSettingsPage from "@/pages/AccountSettingsPage.vue";
import UploadGitHubTokenPage from "@/pages/UploadGitHubTokenPage.vue";
import EachHiringServicePage from "@/pages/EachHiringServicePage.vue";
import AllCandidatesPage from "@/pages/AllCandidatesPage.vue";
import StartInterviewPage from "@/pages/StartInterviewPage.vue";
import TaskBasePage from "@/pages/TaskBasePage.vue";
import EachCategoryPage from "@/pages/EachCategoryPage.vue";
import ApproveEmailPage from "@/pages/ApproveEmailPage.vue";
import ForgotPasswordPage from "@/pages/ForgotPasswordPage.vue";
import AllWorkersPage from "@/pages/AllWorkersPage.vue";

const routes = [
    {
        path: '/',
        name: 'main',
        component: MainPage
    },
    {
        path: '/create-organization',
        name: 'create-organization',
        component: CreateOrganizationPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations',
        name: 'all-organizations',
        component: AllOrganizationsPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:id',
        name: 'each-organization',
        component: EachOrganizationPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:id/workers',
        name: 'all-workers',
        component: AllWorkersPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/login',
        name: 'login',
        component: LoginPage
    },
    {
        path: '/signup',
        name: 'signup',
        component: RegistrationPage
    },
    {
        path: '/forgot/password',
        name: 'forgot-password',
        component: ForgotPasswordPage
    },
    {
        path: '/account',
        name: 'account',
        component: AccountPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:id/workers/create',
        name: 'create-worker',
        component: AddWorkerPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:organization_id/workers/get/:worker_id',
        name: 'get-worker',
        component: EachWorkerPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:organization_id/boards/get/:board_id',
        name: 'get-board',
        component: EachTaskBoardPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:organization_id/boards/get/:board_id/task/:task_id',
        name: 'get-task',
        component: EachTaskPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/account/settings',
        name: 'account-settings',
        component: AccountSettingsPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/account/settings/upload/token',
        name: 'account-settings-upload-token',
        component: UploadGitHubTokenPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/account/settings/approve/email',
        name: 'approve-email',
        component: ApproveEmailPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:id/hiring',
        name: 'each-hiring-service',
        component: EachHiringServicePage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:id/hiring/candidates',
        name: 'all-candidates',
        component: AllCandidatesPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/hiring/candidates/interview/start/:candidate_id',
        name: 'start-interview-with-candidate',
        component: StartInterviewPage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:id/hiring/tasks',
        name: 'hiring-tasks',
        component: TaskBasePage,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/organizations/:id/hiring/category/:category_id',
        name: 'hiring-each-category',
        component: EachCategoryPage,
        meta: {
            requiresAuth: true
        }
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

// eslint-disable-next-line no-unused-vars
router.beforeEach((to, from) => {
    // instead of having to check every route record with
    // to.matched.some(record => record.meta.requiresAuth)
    if (to.meta.requiresAuth && !store.getters.isAuthenticated) {
        // this route requires auth, check if logged in
        // if not, redirect to login page.
        return {
            path: '/login',
            // save the location we were at to come back later
            query: {redirect: to.fullPath},
        }
    }
})

const DEFAULT_TITLE = "Teamsync";
router.afterEach((to) => {
    nextTick(() => {
        document.title = to.meta.title || DEFAULT_TITLE;
    });
});

export default router
