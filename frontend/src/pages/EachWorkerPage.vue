<template>
    <div class="flex justify-center text-sm breadcrumbs mt-10">
        <ul>
            <li><a @click="$router.push('/organizations/' + organization.id)">Компания</a></li>
            <li>Страница сотрудника</li>
        </ul>
    </div>
    <div class="flex justify-center">
        <worker-card
                :worker="this.worker"
                :organization="this.organization"
        />
    </div>

    <div class="flex justify-center p-10 pt-0">
        <add-function-for-worker-form :organization="this.organization" @create="linkFunction"/>
    </div>

</template>

<script>
import axios from "axios";
import store from "@/store";
import WorkerCard from "@/components/ui/WorkerCard.vue";
import AddFunctionForWorkerForm from "@/components/AddFunctionForWorkerForm.vue";

export default {
    components: {AddFunctionForWorkerForm, WorkerCard},
    data() {
        return {
            worker: '',
            organization: {
                id: 0,
                name: '',
                address: '',
                link: '',
                workers: [],
                functions: []
            }
        }
    },
    methods: {
        async getWorker() {
            await axios.get("http://localhost:8080/api/tracker/v1/worker/get/" + this.$route.params.worker_id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);
                this.worker = response.data.payload;
            }).catch((response) => {
                console.log(response.data.payload);
            })
        },
        async getOrganization() {
            await axios.get("http://localhost:8080/api/tracker/v1/organization/getById/" + this.$route.params.organization_id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.organization = response.data.payload;
            }).catch((error) => {
                console.log(error)
            })
        },
        async linkFunction(linkFuncToWorker) {
            await axios.put("http://localhost:8080/api/tracker/v1/worker/addFunctionForWorker", {
                functionId: linkFuncToWorker.selectFuncId,
                workerId: this.$route.params.worker_id,
                tariffRate: linkFuncToWorker.tariffRate
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).catch((response) => {
                console.log(response)
            }).catch((response) => {
                console.log(response)
            })
        }
    },
    mounted() {
        this.$store.commit('setPageName', 'each-worker-page');
        this.getWorker();
        this.getOrganization();
    }
}
</script>