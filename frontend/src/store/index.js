import {createStore} from 'vuex'
import createPersistedState from 'vuex-persistedstate'

export default createStore({
    state: () => ({
        isAuth: false,
        pageName: '',
        user: {
            id: '',
            firstName: '',
            lastName: '',
            isUserAdmin: false
        },
        token: '',
        gitHubToken: ''
    }),
    getters: {
        isAuthenticated: state => state.isAuth
    },
    mutations: {
        setPageName(state, pageName) {
            state.pageName = pageName;
        },
        setAuth(state, auth) {
            state.isAuth = auth;
        },
        setUser(state, user) {
            state.user.id = user.id;
            state.user.firstName = user.firstName;
            state.user.lastName = user.lastName;
            state.user.isUserAdmin = user.isUserAdmin;
        },
        setToken(state, token) {
            state.token = token;
        },
        setGitHubToken(state, gitHubToken) {
            state.gitHubToken = gitHubToken;
        }
    },
    actions: {},
    modules: {},
    plugins: [
        createPersistedState({
            storage: window.sessionStorage
        })
    ]
})
