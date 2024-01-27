import client from "../../../http.common";

export default {
    namespaced: true,
    state: {
        trips: null
    },
    mutations: {
        setTrips(state, trips) {
           state.trips = trips;
        }
    },
    actions: {
        async fetchTrips({ commit }) {
            try {
                await client.get('trips').then((response) => {
                        commit('setTrips', response);
                });
            } catch (error) {
                console.error(error);
                throw error;
            }
        },

        async saveTrip({ commit }, { newTrip, userId }) {
            console.log(userId)
            try {
                const result = await client.post('trips/save/' + userId, newTrip);
                console.log(result.data)
                return result.data;
            } catch (error) {
                console.error(error);
                throw error;
            }
        }
    },
};