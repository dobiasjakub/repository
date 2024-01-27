<template>
  <div class="content-wrapper">

    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0">Cestovní záznamy</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">

            </ol>
          </div>
        </div>
        <div class="content">
          <div class="container-fluid">
            <div class="row">
              <div class="col-lg-12">
                <div class="card">
                  <div class="card-header border-0">
                    <div class="row">
                      <div class="d-flex justify-content-between align-items-center w-100">
                        <!-- Text na levé straně -->
                        <div>{{ formattedCurrentMonth }}</div>
                        <!-- Tlačítko na pravé straně -->
                        <button class="btn btn-primary" @click="createTrip">Vytvořit novou cestu</button>
                      </div>
                    </div>
                  </div>
                  <div class="card-body">
                    <div class="col-sm-12">
                      <div v-if="tripsList.length === 0" style="text-align: center; font-size: 1.2em"><span><i>Žádná data k zobrazení ...</i></span></div>
                      <table v-else class="table table-bordered table-hover table-striped dtr-inline collapsed">
                        <thead>
                        <tr>
                          <th class="sorting" rowspan="1" colspan="1">Datum odjezdu</th>
                          <th class="sorting" rowspan="1" colspan="1">Datum příjezdu</th>
                          <th class="sorting sorting_asc" rowspan="1" colspan="1">Počáteční destinace</th>
                          <th class="sorting" rowspan="1" colspan="1">Cílová destinace</th>
                          <th class="sorting" rowspan="1" colspan="1">Km</th>
                          <th class="sorting" rowspan="1" colspan="1">Délka cesty</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="trip in tripsList" class="odd">
                          <td class="dtr-control" tabindex="0"> {{ getTripStart(trip) }} </td>
                          <td class="sorting_1"> {{ getTripEnd(trip) }} </td>
                          <td> {{ getStartDestination(trip) }} </td>
                          <td> {{ getEndDestination(trip) }} </td>
                          <td> {{ getTripDistance(trip) }} </td>
                          <td> {{ getTripLength(trip) }} </td>
                        </tr>
                        </tbody>
                        <tfoot>
                        <tr>
                          <th rowspan="1" colspan="1">Celkem</th>
                          <th rowspan="1" colspan="1">Browser</th>
                          <th rowspan="1" colspan="1">Platform(s)</th>
                          <th rowspan="1" colspan="1">Engine version</th>
                          <th rowspan="1" colspan="1" style="display: none;">CSS grade</th>
                        </tr>
                        </tfoot>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

     <div v-if="openCreateTripModal"  class="modal-dialog" style="position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); z-index: 1000; width: 30em">

      <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title">Vytvořit novou cestu</h4>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close" @click="closeNewTripModal">
              <span aria-hidden="true">×</span>
            </button>
          </div>
          <div class="modal-body">

            <div v-for="(segment, index) in newTrip.tripSegments" :key="index">
             <span>{{ segment.startDestination }} - {{ segment.endDestination }} </span>
              <button type="button" class="btn btn-danger" @click="removeTripSegment(index)"> - </button>
            </div>
            <button type="button" class="btn btn-primary" @click="createTripSegment"> + </button>
            <div>
            </div>
          </div>
          <div class="modal-footer justify-content-between">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary" @click="saveTrip">Uložit</button>
          </div>
        </div>
      </div>


      <div v-if="openCreateTripSegmentModal"  class="modal-lg" style="position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); z-index: 1000; width: 35em">
      <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title">Vytvořit nový segment cesty</h4>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close" @click="closeNewTripSegmentModal">
              <span aria-hidden="true">×</span>
            </button>
          </div>
          <div class="modal-body">
            <div class="form">
              <div class="form-group">
                <label for="startDestination">Počáteční destinace:</label>
                <input type="text" class="form-control" id="startDestination" v-model="newTripSegment.startDestination">
              </div>
              <div class="form-group">
                <label for="endDestination">Cílová destinace:</label>
                <input type="text" class="form-control" id="endDestination" v-model="newTripSegment.endDestination">
              </div>
              <div class="form-group">
                <label for="distance">Vzdálenost:</label>
                <input type="number" class="form-control" id="distance" v-model.number="newTripSegment.distance">
              </div>
              <div class="form-group">
                <label for="tripStart">Začátek cesty:</label>
                <input type="datetime-local" class="form-control" id="tripStart" v-model="newTripSegment.tripStart">
              </div>
              <div class="form-group">
                <label for="tripEnd">Konec cesty:</label>
                <input type="datetime-local" class="form-control" id="tripEnd" v-model="newTripSegment.tripEnd">
              </div>
              <div class="form-group">
                <label for="travelType">Typ cestování:</label>
                <select class="form-control" id="travelType" v-model="newTripSegment.travelType">
                  <option value="Car">Auto</option>
                  <option value="Train">Vlak</option>
                  <option value="Bus">Autobus</option>
                </select>
              </div>
              <div class="modal-footer justify-content-between">
                <div type="button" class="btn btn-default" data-dismiss="modal" @click="closeNewTripSegmentModal">Zavřít
                </div>
                <div type="button" class="btn btn-primary" @click="saveTripSegment">Uložit</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import moment from "moment";

export default {
  name: "TravelBook",

  data() {
    return {
      content: "",
      view: null,
      openCreateTripModal: false,
      openCreateTripSegmentModal: false,
      newTrip: {
        userId: null,
        tripSegments: [],
      },
      newTripSegment: null
    };
  },
  computed: {
    formattedCurrentMonth() {
      return moment().format('MMMM/yyyy');
    },
    currentUser() {
      return this.$store.state.auth.user;
    },
    tripsList() {
      return this.$store.state.trips.trips.data;
    }
  },
  methods: {
    createTrip() {
      this.newTrip.userId = this.currentUser.userId
      this.openCreateTripModal = true;
    },
    createTripSegment() {
      this.openCreateTripSegmentModal = true

      this.newTripSegment = {
        id: null,
        startDestination: '',
        endDestination: '',
        distance: 0,
        tripStart: null,
        tripEnd: '',
        travelType: '',
      }
    },
    removeTripSegment(i) {
      this.newTrip.tripSegments.splice(i, 1);
    },
    saveTripSegment() {
      this.newTrip.tripSegments.push(this.newTripSegment);
      this.closeNewTripSegmentModal();
    },
    async saveTrip() {
      await this.$store.dispatch("trips/saveTrip", {newTrip: this.newTrip, userId: this.currentUser.userId});
      this.closeNewTripModal();
      this.newTrip.tripSegments = [];

      this.reload()
    },
    closeNewTripSegmentModal() {
      this.openCreateTripSegmentModal = false
    },
    closeNewTripModal() {
      this.openCreateTripModal = false
    },

    getTripStart(trip) {
      return moment(trip.tripSegments[0].startDate).format("D.M.yyyy")
    },
    getTripEnd(trip) {
      return trip.tripSegments.length > 0 ? moment(trip.tripSegments[trip.tripSegments.length - 1].tripStart).format("D.M.yyyy") : null
    },
    getStartDestination(trip) {
      return trip.tripSegments[0].startDestination;
    },
    getEndDestination(trip) {
      return trip.tripSegments.length > 0 ? trip.tripSegments[trip.tripSegments.length - 1].endDestination : null

    },
    getTripDistance(trip) {
      return trip.tripSegments.map(s => s.distance).reduce((a, b) => a + b, 0);
    },
    getTripLength(trip) {
      let timeSum = trip.tripSegments.reduce((a, b) => a + (moment(b.tripStart) - moment(b.tripEnd)), 0);

      let seconds = timeSum / 1000;
      let minutes = Math.floor(seconds / 60);
      seconds = seconds % 60;
      let hours = Math.floor(minutes / 60);
      minutes = minutes % 60;
      let days = Math.floor(hours / 24);
      hours = hours % 24;

      return ` ${days} dní, ` + ` ${hours} hodin,`+ `${minutes} minut`
    },
    async reload() {
      await this.$store.dispatch("trips/fetchTrips")
    }
  },
  created() {
    this.reload()
  }
}
</script>

<style scoped>

</style>