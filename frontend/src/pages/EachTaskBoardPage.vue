<template>
  <each-task-board
          :organization="this.organization"
          :board="this.board"
          :is-current-user-organization-admin="this.isCurrentUserOrganizationAdmin"
  />
</template>

<script>
import axios from "axios";
import store from "@/store";
import EachTaskBoard from "@/components/EachTaskBoard.vue";

export default {
    components: {EachTaskBoard},
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
            board: {
                id: 0,
                name: '',
                boardColumns: []
            },
            isCurrentUserOrganizationAdmin: false
        }
    },
    methods: {
        async getOrganization() {
            await axios.get("http://localhost:8080/api/tracker/v1/organization/getById/" + this.$route.params.organization_id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.organization = response.data.payload;
                this.checkIsCurrentUserAdmin();
                console.log(response.data.payload)
            }).catch((error) => {
                console.log(error)
            })
        },
        async getBoard() {
            await axios.get("http://localhost:8080/api/tracker/v1/taskBoard/get/" + this.$route.params.board_id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.board = response.data.payload;
                console.log(this.board)
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
        this.$store.commit('setPageName', 'each-task-board-page');
        this.getOrganization();
        this.getBoard();
    }
}
</script>