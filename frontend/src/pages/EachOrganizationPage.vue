<template>
    <each-organization
        :organization="this.organization"
        :is-current-user-organization-admin="this.isCurrentUserOrganizationAdmin"
    />
</template>

<script>
import axios from "axios";
import EachOrganization from "@/components/EachOrganization.vue";
import store from "@/store";

export default {
    components: {EachOrganization},
    data() {
        return {
            organization: {
                id: 0,
                name: '',
                address: '',
                link: '',
                workers: [],
                functions: [],
                taskBoards: []
            },
            isCurrentUserOrganizationAdmin: false
        }
    },
    methods: {
        async getOrganization() {
            await axios.get("http://localhost:8080/api/tracker/v1/organization/getById/" + this.$route.params.id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.organization = response.data.payload;
                this.checkIsCurrentUserAdmin();
                console.log(response.data.payload)
                console.log(store.state.token)
            }).catch((error) => {
                console.log(error)
            })
        },
        checkIsCurrentUserAdmin() {
            if (store.state.user.isUserAdmin === true) {
                this.isCurrentUserOrganizationAdmin = true;
            }

            if (this.organization.ownerUserId === store.state.user.id) {
                this.isCurrentUserOrganizationAdmin = true;
            }

            for (let i = 0; i < this.organization.workers.length; i++) {
                if (this.organization.workers[i].userId === store.state.user.id && this.organization.workers[i].isOrganizationAdmin === true) {
                    this.isCurrentUserOrganizationAdmin = true
                }
            }

            console.log("ADMIN? : " + this.isCurrentUserOrganizationAdmin)
        }
    },
    mounted() {
        this.getOrganization();
        this.$store.commit('setPageName', 'each-organization-page');
    }
}
</script>

<style lang="scss" scoped>

</style>