<template>
    <form @submit.prevent method="post">
        <div class="form-control mt-3 mb-3">
            <label class="label">
                <span class="label-text">Выберите новый статус</span>
            </label>
            <select class="select select-bordered w-full" v-model="status.selectId">
                <option disabled selected>Выберите статус</option>
                <option
                        v-for="column in taskBoard.boardColumns" :key="column.id" :value="column.id"
                >
                    {{ column.name }}
                </option>
            </select>
        </div>

        <div class="flex justify-center">
            <success-outline-button @click="changeStatus" class="">Поменять статус</success-outline-button>
        </div>
    </form>
</template>
<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";

export default {
    components: {SuccessOutlineButton},
    props: {
        taskBoard: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            status: {
                selectId: 0,
            }
        }
    },
    methods: {
        changeStatus() {
            this.$emit('create', this.status)
        }
    }
}
</script>