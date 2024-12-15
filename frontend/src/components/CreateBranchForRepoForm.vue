<template>
    <form @submit.prevent method="post">
        <div class="form-control">
            <label class="label">
                <span class="label-text">Выберите репозиторий</span>
            </label>
            <select
                    class="select select-bordered w-full max-w-xs"
                    @change="getBranches"
                    v-model="newBranch.repositoryFullName"
            >
                <option disabled selected>Выберите репозиторий</option>
                <option v-for="repo in this.github.repositories" :key="repo.id" :value="repo.full_name">
                    {{ repo.full_name }}
                </option>
            </select>
        </div>

        <div class="form-control mt-3" v-show="isRepoSelected">
            <label class="label">
                <span class="label-text">Выберите родительскую ветку</span>
            </label>
            <select class="select select-bordered w-full max-w-xs" v-model="newBranch.fromBranchSha">
                <option disabled selected>Выберите ветку</option>
                <option v-for="branch in this.branches" :key="branch.id" :value="branch.commit.sha">
                    {{ branch.name }}
                </option>
            </select>
        </div>

        <text-input
                v-show="isRepoSelected"
                label="Название новое ветки"
                placeholder="Например, new_branch"
                class="mt-3"
                v-model="newBranch.newBranchName"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createNewBranch" class="">Создать ветку</success-outline-button>
        </div>
    </form>
</template>
<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextInput from "@/components/ui/TextInput.vue";
import axios from "axios";
import store from "@/store";

export default {
    components: {TextInput, SuccessOutlineButton},
    props: {
        github: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            isRepoSelected: false,
            branches: [],
            newBranch: {
                repositoryFullName: '',
                fromBranchSha: '',
                newBranchName: ''
            }
        }
    },
    methods: {
        async getBranches(e) {
            await axios.get("https://api.github.com/repos/" + e.target.value + "/branches?per_page=100", {
                headers: {
                    'Accept': 'application/vnd.github+json',
                    'Authorization': 'Bearer ' + store.state.gitHubToken,
                    'X-GitHub-Api-Version': '2022-11-28'
                }
            }).then((response) => {
                this.isRepoSelected = true;
                this.branches = response.data;
            }).catch((response) => {
                console.log(response)
            })
        },
        createNewBranch() {
            this.$emit('create', this.newBranch)
        }
    }
}
</script>