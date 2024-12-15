<template>
    <error-alert
            class="w-1/2 m-auto mt-10"
            :is-error="isError"
    > {{ errorDescription }}
    </error-alert>
    <organization-list :organizations="organizations"/>
</template>

<script>
import axios from "axios";
import OrganizationList from "@/components/OrganizationList.vue";
import store from "@/store";
import Constants from "@/components/constants";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";

export default {
    components: {ErrorAlert, OrganizationList},
    data() {
        return {
            pageName: 'all-organizations-page',
            organizations: [],
            isError: false,
            errorDescription: ''
        }
    },
    methods: {
        async getOrganizations() {
            await axios.get('http://localhost:8080/api/tracker/v1/organization/getAll', {
                headers: {
                    "Authorization": store.state.token
                }
            }).then((response) => {
                this.organizations = response.data.payload;
            }).catch((response) => {
                if (response.response.data === Constants.PERMISSION_DESCRIPTION) {
                    this.isError = true;
                    this.errorDescription = "У вас нет прав на это действие. Пожалуйста, попробуйте позднее."
                }
            })
        }
    },
    mounted() {
        this.getOrganizations();
        this.$store.commit('setPageName', 'all-organizations-page')
    }
}
</script>

<style lang="scss" scoped>

</style>