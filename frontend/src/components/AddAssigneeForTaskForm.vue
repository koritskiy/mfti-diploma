<template>
    <form @submit.prevent method="post">
        <div class="form-control mt-3 mb-3">
            <label class="label">
                <span class="label-text">Выберите сотрудника</span>
            </label>
            <select class="select select-bordered w-full" v-model="worker.selectId">
                <option disabled selected>Выберите сотрудника</option>
                <option
                    v-for="worker in organization.workers" :key="worker.id" :value="worker.id"
                >
                    {{ worker.lastName }} {{ worker.firstName }}
                </option>
            </select>
        </div>

        <div class="flex justify-center">
            <success-outline-button @click="linkWorker" class="">Привязать</success-outline-button>
        </div>
    </form>
</template>
<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";

export default {
    components: {SuccessOutlineButton},
    props: {
        task: {
            type: Object,
            required: true
        },
        organization: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            worker: {
                selectId: 0
            }
        }
    },
    methods: {
        linkWorker() {
            this.$emit('create', this.worker)
        }
    }
}
</script>